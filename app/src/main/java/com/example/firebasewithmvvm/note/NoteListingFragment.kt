package com.example.firebasewithmvvm.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.firebasewithmvvm.R
import com.example.firebasewithmvvm.databinding.FragmentNoteListingBinding
import com.example.firebasewithmvvm.model.Note
import com.example.firebasewithmvvm.util.UiState
import com.example.firebasewithmvvm.util.hide
import com.example.firebasewithmvvm.util.show
import com.example.firebasewithmvvm.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListingFragment : Fragment() {

    private var _binding: FragmentNoteListingBinding? = null
    private val binding get() = _binding!!

    val viewModel: NoteViewModel by viewModels()
    var deletePosition: Int = -1

    val adapter by lazy {
        NoteListingAdapter(
            onItemClicked = { pos, item ->
                findNavController().navigate(R.id.action_noteListingFragment_to_noteDetailFragment,Bundle().apply {
                    putString("type","view")
                    putParcelable("note",item)
                })
            },
            onEditClicked = { pos, item ->
                findNavController().navigate(R.id.action_noteListingFragment_to_noteDetailFragment,Bundle().apply {
                    putString("type","edit")
                    putParcelable("note",item)
                })
            },
            onDeleteClicked = { pos, item ->
                deletePosition = pos
                viewModel.deleteNote(item)
            }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNoteListingBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recyclerView adapter initialize
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = staggeredGridLayoutManager
        binding.recyclerView.itemAnimator = null

        // not ekleme ekranına gitme
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_noteListingFragment_to_noteDetailFragment,Bundle().apply {
                putString("type","create")
            })
        }

        // önce viewmodel'daki "repo'dan verileri çeken fonksiyon" çalıştırılır.
        viewModel.getNotes()

        // viewmodel'daki getNotes() fonksiyonu, verileri yine viewmodel'daki _notes'a aktarır.
        // biz de "notes" liveData'sını observe ederek verinin durumunu takip ederiz.
        viewModel.notes.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    adapter.updateList(state.data.toMutableList())
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }
            }
        }

        viewModel.deleteNote.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    toast(state.data)
                    adapter.removeItem(deletePosition)

                    // ALTTAKİ İŞLEMDE;
                    // notes'u observe ederken success alırsak UiState bize List<Note> döndürür.
                    // bu List<Note>'u adapter'daki updateList fonksiyonuna veriyoruz.
                    // ayrıca List<Note>'u, bu fragment'ta oluşturduğumuz "list"e atıyoruz.
                    // Eğer silme işlemi olursa;
                    // 1) elimizdeki "list" ten gerekli item silinir ve yeni liste adapter'daki updateList fonksiyonuna verilir.
                    // fakat bu sadece bir item silmek için adapter'daki tüm list'i değiştirmeye neden olur yani verimsizdir.
                    // 2) "notes"u observe ederken UiState'den aldığımız List<Notu>'u bu fragment'ta oluşturduğumuz "list"e atmak yerine
                    // adapter'daki removeItem fonksiyonu çağırılıp silinmesi gereken item'ın pozisyonu verilebilir. Bu sayede adapter'daki list'te silme işlemi olur
                    // yani tüm liste yenilenmez.
                    // İkinci işlemin crash vermemesi için recyclerView'in "itemAnimator"üne null veriyoruz.

                    /*
                    if(deletePosition != -1) {
                        list.removeAt(deletePosition)
                        adapter.updateList(list)
                    }
                     */
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}