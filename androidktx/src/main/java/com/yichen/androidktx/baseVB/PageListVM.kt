package com.yichen.androidktx.baseVB

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.yichen.androidktx.core.*
import com.yichen.androidktx.livedata.StateLiveData
import com.yichen.statelayout.StateLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import java.lang.IllegalArgumentException
import java.util.concurrent.CopyOnWriteArrayList


/**
 * 注意一定要实现getDiffCallback，否则数据更新只能采用 notifyDataSetChanged()
 */
abstract class PageListVM<T> : ViewModel(),
    OnRefreshLoadMoreListener {
    var firstLoad = true
    var startPage = 1
    var page = startPage
    var hasMore = true
    var listData = StateLiveData<CopyOnWriteArrayList<T>>()
    var oldData = arrayListOf<T>()

    init {
        listData.value = CopyOnWriteArrayList()
    }

    var onRefreshCB: (() -> Unit)? = null
    var onLoadMoreCB: (() -> Unit)? = null
    open fun bindRecyclerView(
        owner: LifecycleOwner,
        rv: RecyclerView?,
        smartRefresh: SmartRefreshLayout?,
        stateLayout: StateLayout? = null,
        firstShowLoading: Boolean = false,
        autoLoadData: Boolean = true,
        delay: Long = 250,//加一点延迟，loading闪烁
        isForceContent: Boolean = false,//state为Empty为空的时候也显示content
        onRefresh: (() -> Unit)? = null,
        onLoadMore: (() -> Unit)? = null,
        onDataUpdate: (() -> Unit)? = null,
    ) {
        onRefreshCB = onRefresh
        onLoadMoreCB = onLoadMore
        listData.observe(owner, Observer {
            val diffCallback = getDiffCallback(oldData, it)
            if (diffCallback != null) {
                rv?.diffUpdate(diffCallback)
            } else {
                rv?.adapter?.notifyDataSetChanged()
            }
            onDataUpdate?.invoke()
        })
        listData.state.observe(owner, Observer {
            var success = true
            when (it) {
                StateLiveData.State.Loading -> {
                    if (firstLoad && firstShowLoading) {
                        stateLayout?.showLoading()
                    }
                }

                StateLiveData.State.Empty -> {


                    if (listData.value!!.isNullOrEmpty()) {
                        if (isForceContent) {
                            stateLayout?.showContent()
                        } else {
                            stateLayout?.showEmpty()
                        }

                    } else {
                        stateLayout?.postDelayed({
                            stateLayout.showContent()
                        }, delay)
                    }
                }

                StateLiveData.State.Error -> {
                    success = false
                    if (listData.value!!.isNullOrEmpty()) {
                        stateLayout?.showError()
                    } else {
                        stateLayout?.postDelayed({
                            stateLayout.showContent()
                        }, delay)

                    }
                }

                else -> {
                    stateLayout?.postDelayed({
                        stateLayout.showContent()
                    }, delay)
                }
            }
            if (it != StateLiveData.State.Loading) {
                smartRefresh?.finishRefresh(success)
                smartRefresh?.finishLoadMore(success)
                smartRefresh?.setNoMoreData(!hasMore)
            }
        })
        smartRefresh?.setOnRefreshLoadMoreListener(this)

        if (firstShowLoading && stateLayout != null) {
            LogUtils.dTag("test", "showLoading")
            stateLayout.showLoading()

            if (autoLoadData) {
                stateLayout.post { refresh() } //stateLayout绘制完成后再请求，否则请求太快，loading还没绘制好，就出数据了
            }
        } else {
            if (autoLoadData) smartRefresh?.post { smartRefresh.autoRefresh() }
        }
    }

    open fun refresh() {
        LogUtils.dTag("test", "refresh")
        page = startPage
        load()
        onRefreshCB?.invoke()
    }


    /**
     * 新增数据
     */
    fun insert(t: T, position: Int? = null) {
        val list = listData.value!!
        if (position == null) {
            updateOldData()
            list.add(t)
            listData.postValueAndSuccess(list)
        } else if (position >= 0 && list.size > position) {
            updateOldData()
            list.add(position!!, t)
            listData.postValueAndSuccess(list)
        }
    }

    /**
     * 更新数据，如果直接修改指定位置的bean，会同步更新old；导致old和new是一样的数据。
     * 推荐将目标数据deepCopy()后再进行修改，这样不会同步更新old
     */
    fun update(position: Int, t: T) {
        val list = listData.value!!
        if (position < 0 || list.size <= position) return
        updateOldData()
        list[position] = t
        listData.postValueAndSuccess(list)
    }

    /**
     * 删除数据
     */
    fun remove(position: Int) {
        val list = listData.value!!
        if (position < 0 || list.size <= position) return
        updateOldData()
        list.removeAt(position)
        listData.postValueAndSuccess(list)
    }

    /**
     * 清空数据
     */
    fun clear() {
        val list = listData.value!!
        if (list.isEmpty()) return
        updateOldData()
        list.clear()
        listData.postValueAndSuccess(list)
    }

    open fun loadMore() {
        if (hasMore) {
            page += 1
            load()
            onLoadMoreCB?.invoke()
        }
    }

    abstract fun load()

    open fun processData(data: List<T>?, nullIsEmpty: Boolean = false) {
        firstLoad = false
        if (page == startPage) listData.value!!.clear()
        if (data != null) {
            val list = listData.value
            updateOldData()
            if (data.isNotEmpty()) {
                hasMore = true
                list?.addAll(data)
                listData.postValueAndSuccess(list!!)
            } else {
                //listWrapper数据为空
                hasMore = false
                if (list!!.isEmpty()) listData.postEmpty(list)
                else listData.postValueAndSuccess(list)
            }
        } else {
            val list = listData.value
            updateOldData()
            if (list.isNullOrEmpty()) {
                if (nullIsEmpty) listData.postEmpty(list)
                else listData.postError()
            } else {
                listData.postError()
            }
        }
    }

    /**
     * 由于泛型无法继承，该方法必须子类来实现，实现的模板代码如下：
     * oldData = listData.value!!.deepCopy<ArrayList<T>>()
     */
    open fun updateOldData() {
        if (getDiffCallback(oldData, listData.value!!) == null) return
        throw IllegalArgumentException("updateOldData方法未实现，实现方式固定为：oldData = listData.value!!.deepCopy<ArrayList<T>>() ")
    }

    /**
     * 需要同时配套实现 updateOldData() 方法，实现代码为固定写法 oldData = listData.value!!.deepCopy<ArrayList<T>>() 。
     * demo如下：
     * class UserDiffCallback(oldData: List<User>?, newData: List<User>?) : DiffCallback<User>(oldData, newData) {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    if(oldData.isNullOrEmpty() || newData.isNullOrEmpty()) return false
    return oldData!![oldItemPosition].id == newData!![newItemPosition].id
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    return oldData!![oldItemPosition].name == newData!![newItemPosition].name
    }
    //局部更新
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
    val oldItem = oldData!![oldItemPosition]
    val newItem = newData!![newItemPosition]
    val bundle = Bundle()
    if(oldItem.name != newItem.name){
    bundle.putString("name", newItem.name)
    }
    return bundle
    }
    }
     */
    open fun getDiffCallback(old: List<T>, new: List<T>): DiffCallback<T>? = null

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        loadMore()
    }

    fun reset() {
        page = startPage
        hasMore = true
        listData.postValueAndSuccess(CopyOnWriteArrayList())
    }
}