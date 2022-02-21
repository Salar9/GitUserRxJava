package com.example.den.gituserrxjava.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.den.gituserrxjava.R
import com.example.den.gituserrxjava.databinding.GitUserListItemBinding
import com.example.den.gituserrxjava.databinding.MainFragmentBinding
import com.example.den.gituserrxjava.model.GitUser
import io.reactivex.disposables.CompositeDisposable


private const val TAG = "MainFragment"

class MainFragment : Fragment() {
    private lateinit var binding: MainFragmentBinding
    private var compositeDisposable = CompositeDisposable()
    private var isLoading = false

    companion object {
        fun newInstance() = MainFragment()
    }
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(viewModel.users.isEmpty()) {
            val subscribe = viewModel.gitRepo.getUsers().subscribe(
                    { result ->
                        result.forEach { user ->
                            Log.i(TAG, "Name - ${user.login}\tAvatar - ${user.avatar_url}")
                            viewModel.users.add(user)
                        }
                    },
                    { error -> error.printStackTrace() },
                    { binding.recyclerView.adapter = GitUserAdapter(viewModel.users) })
            compositeDisposable.add(subscribe)

        }
        else binding.recyclerView.adapter = GitUserAdapter(viewModel.users)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val count = (layoutManager as LinearLayoutManager).itemCount
                    val pos = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    if(!isLoading && pos > count-10){
                        isLoading = true

                        val subscribe = viewModel.gitRepo.getPage(viewModel.users.last().id).subscribe(
                                { result ->
                                    result.forEach { user ->
                                        Log.i(TAG, "Scroll: Name - ${user.login}\tAvatar - ${user.avatar_url}")
                                        viewModel.users.add(user)
                                        recyclerView.adapter?.notifyDataSetChanged()
                                    }
                                },
                                { error -> error.printStackTrace() },
                                { isLoading = false }
                            )
                        compositeDisposable.add(subscribe)
                    }
                }
            })
        }
        return binding.root
    }

    private inner class GitUserHolder(private val binding: GitUserListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.gitUserViewModel = GitUserViewModel()
        }

        fun bind(user: GitUser) {
            binding.apply {
                gitUserViewModel?.user = user
                //помойму не совсем првильно загружать аватары тут, имхо это ломает логику MVVM, но как по другому пока не придумал :(
                viewModel.gitRepo.getBitmapAvatar(user.avatar_url,imageView)
                val subscribe = viewModel.gitRepo.getUserDetail(user.login).subscribe(
                    {result ->
                        Log.i(TAG,"Detail - ${result.login}\t${result.name}\t${result.company}\t${result.location}")
                        gitUserViewModel?.userDetail = result
                    },
                    { error -> error.printStackTrace() }
                )
                compositeDisposable.add(subscribe)

                executePendingBindings()
            }
        }
    }

    private inner class GitUserAdapter(private val users: List<GitUser>) : RecyclerView.Adapter<GitUserHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitUserHolder {
            val binding = DataBindingUtil.inflate<GitUserListItemBinding>(layoutInflater, R.layout.git_user_list_item, parent,false)
            return GitUserHolder(binding)
        }
        override fun onBindViewHolder(holder: GitUserHolder, position: Int) {
            holder.bind(users[position])

        }
        override fun getItemCount() = users.size
    }


}