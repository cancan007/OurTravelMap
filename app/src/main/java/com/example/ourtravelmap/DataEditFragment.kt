package com.example.ourtravelmap

import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ourtravelmap.databinding.FragmentDataEditBinding
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [DataEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DataEditFragment : Fragment() {
    // TODO: Rename and change types of parameters
    // フラグメント内でbindingを使う時の典型文
    private var _binding: FragmentDataEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var realm: Realm

    private val args: DataEditFragmentArgs by navArgs()  // nav_graph.xmlのdataEditFragmentのArguments

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDataEditBinding.inflate(inflater, container, false)  // 画面生成
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(args.dataId != -1L){ // 新規登録の場合はデフォルトの-1が返ってくるので、ここは更新時の処理
            val data = realm.where<Data>()
                .equalTo("id", args.dataId).findFirst()
            binding.dateEdit.setText(DateFormat.format("yyyy/MM/dd", data?.date))
            binding.locationEdit.setText(data?.location)
            binding.latitudeEdit.setText(data?.lat.toString())
            binding.longitudeEdit.setText(data?.lng.toString())
            binding.titleEdit.setText(data?.title)
            binding.contentEdit.setText(data?.detail)
            binding.delete.visibility = View.VISIBLE
        }else{
            binding.delete.visibility = View.INVISIBLE
        }
        (activity as? DataActivity)?.setFabVisible(View.INVISIBLE)  // DataActivityクラスのsetFabVisibleを使って、fabボタンを非表示
        binding.save.setOnClickListener{saveData(it)}
        binding.delete.setOnClickListener{deleteData(it)}
    }

    private fun saveData(view: View){
        when(args.dataId) {
            -1L -> {
                realm.executeTransaction { db: Realm ->
                    val maxId = db.where<Data>().max("id")
                    val nextId = (maxId?.toLong() ?: 0L) + 1L
                    val data = db.createObject<Data>(nextId)
                    val date = "${binding.dateEdit.text} ${binding.timeEdit.text}"
                        .toDate()
                    // データ項目のクラス(データベース)に値を書き込む
                    if (date != null) data.date = date
                    data.location = binding.locationEdit.text.toString()
                    //lat, lngに関しては MapActivityのほうで登録できる道も模索中
                    data.lat = binding.latitudeEdit.text.toString().toInt()
                        .toDouble()  // Double型にするには、一度数値型に直さないといけない
                    data.lng = binding.longitudeEdit.text.toString().toInt().toDouble()
                    data.title = binding.titleEdit.text.toString()
                    data.detail = binding.contentEdit.text.toString()
                }
                Snackbar.make(view, "You are successful adding log!", Snackbar.LENGTH_SHORT)  // LENGTH_SHORT: スナックバー表示時間を短く設定
                .setAction("back") { findNavController().popBackStack() }
                    .setActionTextColor(Color.YELLOW)
                    .show()
            }
            else ->{
                realm.executeTransaction { db: Realm ->
                    val data = db.where<Data>().equalTo("id", args.dataId).findFirst()
                    val date = ("${binding.dateEdit.text}" + "${binding.timeEdit.text}").toDate()
                    if(date != null) data?.date = date
                    data?.location = binding.locationEdit.text.toString()
                    data?.lat = binding.latitudeEdit.text.toString().toInt().toDouble()
                    data?.lng = binding.longitudeEdit.text.toString().toInt().toDouble()
                    data?.title = binding.titleEdit.text.toString()
                    data?.detail = binding.contentEdit.text.toString()

                    }
                Snackbar.make(view, "You are successful uploading log!", Snackbar.LENGTH_SHORT)
                    .setAction("back"){findNavController().popBackStack()}
                    .setActionTextColor(Color.YELLOW)
                    .show()

            }
        }
    }

    // 特定のidのデータをデータベースから削除
    private fun deleteData(view:View){
        realm.executeTransaction { db: Realm ->
            db.where<Data>().equalTo("id", args.dataId)?.findFirst()?.deleteFromRealm()
        }
        Snackbar.make(view, "Your log was deleted", Snackbar.LENGTH_SHORT)
            .setActionTextColor(Color.YELLOW)
            .show()

        findNavController().popBackStack()
    }

    override fun onDestroyView(){  // viewが破棄されるときに呼ばれる
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy(){  // Fragmentが破棄されるときに呼ばれる
        super.onDestroy()
        realm.close()  // データベースを閉じる
    }

    // String型の日時をDate型に変換する関数
    private fun String.toDate(pattern: String = "yyyy/MM/dd HH:mm"): Date?{
        return try {
            SimpleDateFormat(pattern).parse(this)
        } catch (e: IllegalArgumentException){
            return null
        } catch (e: ParseException){
            return null
        }
    }
}