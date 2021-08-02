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

    init{
        setHasStableIds(true)  // Data項目(Data.kt)に固有のパラメータidがあるので、ここでそれを使うことを宣言
    }

    class ViewHolder(cell: View): RecyclerView.ViewHolder(cell){  //アダプター内部用のクラス
        val date: TextView = cell.findViewById(android.R.id.text1)  // android.R.id.text1: デフォルトのもので、 onBindViewHolderで別のものにすぐ更新するので、ここはなんでもいい
        val title: TextView = cell.findViewById(android.R.id.text2)
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
        holder.title.text = data?.title // Dataクラスのtitleを表示
    }

    // データ項目のidを返す
    override fun getItemId(position: Int): Long{
        return getItem(position)?.id ?:0  // Dataクラスのidがnullだったら0を返す
    }
}