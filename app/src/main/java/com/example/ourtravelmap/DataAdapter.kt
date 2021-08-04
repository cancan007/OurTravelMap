package com.example.ourtravelmap

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import androidx.recyclerview.widget.RecyclerView

class DataAdapter(data: OrderedRealmCollection<Data>) :
    RealmRecyclerViewAdapter<Data, DataAdapter.ViewHolder>(data, true){

    // Adapterがタップされたことをアクティビティ(DataEditFragment)に通知するコールバック
    private var listener: ((Long?) -> Unit)? = null  //引数がLong型で,戻り値がない関数型の変数listenerを宣言

    // 関数が関数を受け取る(他のクラスから受けっとた関数を自分のところで保持しておくためではないかと予想される)
    fun setOnItemClickListener(listener:(Long?) -> Unit){
        this.listener = listener
    }

    init{
        setHasStableIds(true)  // Data項目(Data.kt)に固有のパラメータidがあるので、ここでそれを使うことを宣言
    }

    class ViewHolder(cell: View): RecyclerView.ViewHolder(cell){  //アダプター内部用のクラス
        val date: TextView = cell.findViewById(android.R.id.text1)  // android.R.id.text1: android標準のリストビューsimple_list_item2のデフォルトのもの
        val location: TextView = cell.findViewById(android.R.id.text2)
    }

    // 新しいViewHolderオブジェクト(セル)を生成するための実装
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataAdapter.ViewHolder {  // ViewGroup: Viewを子として保持することができるもの
        val inflater = LayoutInflater.from(parent.context)  // Viewオブジェクトをインスタンス化
        val view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false)  //inflate: xmlファイルから画面生成
        return ViewHolder(view)  // 新しいセルのインスタンスを返す
    }

      //ViewHolder(セル)の表示内容を更新する
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: Data? = getItem(position)  // getItem: Dataクラス(データモデル)を総称型で指定したので、戻り値はData(positionはアダプターのデータセット内における位置)
        holder.date.text = DateFormat.format("yyyy/MM/dd HH:mm", data?.date)  //Dataクラスのdateを形を変えて表示
        holder.location.text = data?.location // Dataクラスのtitleを表示
        holder.itemView.setOnClickListener { //
            listener?.invoke(data?.id)  // invoke: 関数型の変数に引数を渡して実行
        }
    }

    // データ項目のidを返す
    override fun getItemId(position: Int): Long{
        return getItem(position)?.id ?:0  // Dataクラスのidがnullだったら0を返す
    }
}