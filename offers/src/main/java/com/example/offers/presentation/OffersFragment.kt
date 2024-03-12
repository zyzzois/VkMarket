package com.example.offers.presentation

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.example.core_data.models.Category
import com.example.offers.R
import com.example.offers.databinding.FragmentOffersBinding
import com.example.offers.di.DaggerOffersComponent
import com.example.offers.di.OffersScreenDependenciesProvider
import com.example.offers.presentation.recycler.OfferListLoadStateAdapter
import com.example.offers.presentation.recycler.OffersListAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class OffersFragment : Fragment() {
    private var _binding: FragmentOffersBinding? = null
    private val binding: FragmentOffersBinding
        get() = _binding ?: throw RuntimeException("error")

    private val listAdapter by lazy {
        OffersListAdapter()
    }

    private val component = DaggerOffersComponent
        .builder()
        .dependencies(OffersScreenDependenciesProvider.dependencies)
        .build()

    private val viewModel by lazy {
        component.getOffersViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOffersBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val warningDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.warning_title)
            .setMessage(R.string.warning_message)
            .setPositiveButton(R.string.accept_warning) { _, _ -> }
            .setNeutralButton(R.string.enable_connection) { _, _ ->
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                startActivity(intent)
            }
            .setIcon(R.drawable.icon_connection_lost)
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        setupRecyclerView()
        setupClicks()
        registerForContextMenu(binding.searchButton)
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Initial -> viewModel.updateOffersWithAllOffers()
                is State.Loading -> binding.recyclerView.visibility = View.GONE
                is State.Error -> {
                    warningDialog.show()
                    binding.recyclerView.visibility = View.GONE
                }
                is State.Content -> {
                    warningDialog.dismiss()
                    binding.recyclerView.visibility = View.VISIBLE
                    listAdapter.submitData(viewLifecycleOwner.lifecycle, state.offersList)
                }
            }
        }
    }

    private fun setupClicks() = with(binding) {
        listAdapter.onItemClickListener = { offer ->
            val uri = viewModel.getUriFromModel(offer)
            findNavController().navigate(uri, navOptions, null)
        }
        swipeToRefreshLayout.setOnRefreshListener {
            viewModel.updateOffersWithAllOffers()
            swipeToRefreshLayout.isRefreshing = false
        }
        searchButton.setOnClickListener {
            setupCategoriesPopup(it, R.menu.sort_menu)
        }
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.updateOffersWithQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    viewModel.updateOffersWithAllOffers()
                } else {
                    viewModel.updateOffersWithQuery(newText)
                }
                return true
            }
        })
    }

    private fun setupCategoriesPopup(v: View, @MenuRes menuRes: Int)  {
        val popup = PopupMenu(context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when(menuItem.itemId) {
                R.id.smartphone -> {
                    viewModel.updateOffersWithCategory(Category.SMARTPHONES)
                    true
                }
                R.id.laptops -> {
                    viewModel.updateOffersWithCategory(Category.LAPTOPS)
                    true
                }
                R.id.fragrances -> {
                    viewModel.updateOffersWithCategory(Category.FRAGRANCES)
                    true
                }
                R.id.skincare -> {
                    viewModel.updateOffersWithCategory(Category.SKINCARE)
                    true
                }
                R.id.groceries -> {
                    viewModel.updateOffersWithCategory(Category.GROCERIES)
                    true
                }
                R.id.homeDecor -> {
                    viewModel.updateOffersWithCategory(Category.HOME_DECORATION)
                    true
                }
                R.id.furniture -> {
                    viewModel.updateOffersWithCategory(Category.FURNITURE)
                    true
                }
                R.id.tops -> {
                    viewModel.updateOffersWithCategory(Category.TOPS)
                    true
                }
                R.id.womens_dresses -> {
                    viewModel.updateOffersWithCategory(Category.WOMEN_DRESSES)
                    true
                }
                R.id.man_shirts -> {
                    viewModel.updateOffersWithCategory(Category.MEN_SHIRTS)
                    true
                }
                R.id.man_shoes -> {
                    viewModel.updateOffersWithCategory(Category.MEN_SHOES)
                    true
                }
                R.id.mens_watches -> {
                    viewModel.updateOffersWithCategory(Category.MEN_WATCHES)
                    true
                }
                R.id.womens_watches -> {
                    viewModel.updateOffersWithCategory(Category.WOMEN_WATCHES)
                    true
                }
                R.id.womens_bags -> {
                    viewModel.updateOffersWithCategory(Category.WOMEN_BAGS)
                    true
                }
                R.id.womens_jewellery -> {
                    viewModel.updateOffersWithCategory(Category.WOMEN_JEWELLERY)
                    true
                }
                R.id.sunglasses -> {
                    viewModel.updateOffersWithCategory(Category.SUNGLASSES)
                    true
                }
                R.id.automotive -> {
                    viewModel.updateOffersWithCategory(Category.AUTOMOTIVE)
                    true
                }
                R.id.motorcycle -> {
                    viewModel.updateOffersWithCategory(Category.MOTORCYCLE)
                    true
                }
                R.id.lighting -> {
                    viewModel.updateOffersWithCategory(Category.LIGHTING)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }


    private fun setupRecyclerView() {
        binding.recyclerView.adapter = listAdapter.withLoadStateFooter(OfferListLoadStateAdapter())
        listAdapter.addLoadStateListener { state: CombinedLoadStates ->
            binding.progressBar.isVisible = state.refresh == LoadState.Loading
            if (state.refresh is LoadState.Error) {
                warningDialog.show()
            }
        }
    }
}