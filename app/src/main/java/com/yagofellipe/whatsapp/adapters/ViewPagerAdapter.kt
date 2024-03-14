package com.yagofellipe.whatsapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yagofellipe.whatsapp.fragments.ContatosFragment
import com.yagofellipe.whatsapp.fragments.ConversasFragment

class ViewPagerAdapter(
    val abas: List<String>,
    fragmentStateAdapter: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentStateAdapter, lifecycle) {
    override fun getItemCount(): Int {
        return abas.size
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            1 -> return ContatosFragment()
        }
        return ConversasFragment()
    }
}