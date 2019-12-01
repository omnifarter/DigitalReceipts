package com.example.digitalreceipts.Billsplit;

import android.util.SparseArray;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.jetbrains.annotations.NotNull;

/*
   Extension of FragmentStatePagerAdapter which intelligently caches 
   all active fragments and manages the fragment lifecycles. 
   Usage involves extending from SmartFragmentStatePagerAdapter as you would any other PagerAdapter.
*/
public abstract class SmartFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
	// Sparse array to keep track of registered fragments in memory
	private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

	SmartFragmentStatePagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	// Register the fragment when the item is instantiated
	@NotNull
	@Override
	public Object instantiateItem(@NotNull ViewGroup container, int position) {
		Fragment fragment = (Fragment) super.instantiateItem(container, position);
		registeredFragments.put(position, fragment);
		return fragment;
	}

	// Unregister when the item is inactive
	@Override
	public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
		registeredFragments.remove(position);
		super.destroyItem(container, position, object);
	}

	// Returns the fragment for the position (if instantiated)
	public Fragment getRegisteredFragment(int position) {
		return registeredFragments.get(position);
	}
}