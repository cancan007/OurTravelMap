package com.example.ourtravelmap

import com.google.android.gms.maps.model.LatLng
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

// データを格納するモデル用のクラス
open class Data: RealmObject() {
    @PrimaryKey
    var id: Long = 0  // idでそれぞれのdataを管理するために, @PrimaryKeyを付加
    var date: Date = Date()  // Date(): 現在時刻
    var location: String = ""
    var title: String = ""
    var lat: Double = 0.0
    var lng: Double = 0.0
    var detail: String = ""
}