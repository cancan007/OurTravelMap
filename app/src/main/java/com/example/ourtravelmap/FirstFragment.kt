package com.example.ourtravelmap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ourtravelmap.databinding.FragmentFirstBinding
import io.realm.Realm
import io.realm.kotlin.where

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()  // Realmのインスタンスを取得
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {   // onCreateViewで生成したviewの初期化はここで行う
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.dataList.layoutManager = LinearLayoutManager(context)  //  RecyclerView(dataList)のレイアウトマネージャーとして登録(LinearLayoutManager: 項目を直列に並べる)(contextはアプリの状態ていうイメージ)
        val datas = realm.where<Data>().findAll()  // データ項目を取得
        // adapterを設定
        val adapter = DataAdapter(datas)
        binding.dataList.adapter = adapter

        // Safe Argsという画面遷移時のデータの明け渡しを安全かつ簡単に実装する(dataEditFragmentのArgumentsで設定したdataIdにfirstFragemntからデータを渡す)
        adapter.setOnItemClickListener { id ->
            id?.let{
                val action =
                    FirstFragmentDirections.actionToDataEditFragment(it)  // idをDataEditFragmentに渡している
                findNavController().navigate(action) // navigate:画面遷移しながら上記を実行
            }
        }
        (activity as? DataActivity)?.setFabVisible(View.VISIBLE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy(){  // アクティビティ終了時
        super.onDestroy()
        realm.close()   // Realmのインスタンスを破棄し、リソースを開放
    }
}