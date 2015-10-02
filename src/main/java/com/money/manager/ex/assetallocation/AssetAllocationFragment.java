/*
 * Copyright (C) 2012-2015 The Android Money Manager Ex Project Team
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.money.manager.ex.assetallocation;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.ListView;

import com.money.manager.ex.R;
import com.money.manager.ex.common.BaseListFragment;
import com.money.manager.ex.common.MmexCursorLoader;
import com.money.manager.ex.datalayer.AssetClassRepository;

/**
 * A placeholder fragment containing a simple view.
 */
public class AssetAllocationFragment
    extends BaseListFragment
    implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ASSET_CLASSES = 1;

    public AssetAllocationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_asset_allocation, container, false);
//
//        return view;
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText(getActivity().getResources().getString(R.string.asset_classes_empty));

        // create and link the adapter
        AssetAllocationAdapter adapter = new AssetAllocationAdapter(getActivity(), null);
        setListAdapter(adapter);

        registerForContextMenu(getListView());
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        setListShown(false);
        loadData();

        setFloatingActionButtonVisible(true);
        setFloatingActionButtonAttachListView(true);
    }

    @Override
    public String getSubTitle() {
//        return getString(R.string.asset_allocation);
        return null;
    }

    @Override
    public void onFloatingActionButtonClickListener() {
        startEditAssetClassActivity();
    }

    // data loader

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ASSET_CLASSES:
                // create mmex cursor loader
                AssetClassRepository repo = new AssetClassRepository(getActivity());

                return new MmexCursorLoader(getActivity(), repo.getUri(),
                    repo.getAllColumns(),
                    null, // where
                    null, // args
                    null // sort
                );
                //break;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ASSET_CLASSES:
                AssetAllocationAdapter adapter = (AssetAllocationAdapter) getListAdapter();
                adapter.swapCursor(data);

                if (isResumed()) {
                    setListShown(true);
                    if (data != null && data.getCount() <= 0 && getFloatingActionButton() != null)
                        getFloatingActionButton().show(true);
                } else {
                    setListShownNoAnimation(true);
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_ASSET_CLASSES:
                AssetAllocationAdapter adapter = (AssetAllocationAdapter) getListAdapter();
                adapter.swapCursor(null);
                break;
        }
    }

    // private

    private void startEditAssetClassActivity() {
        Intent intent = new Intent(getActivity(), AssetClassEditActivity.class);
        intent.setAction(Intent.ACTION_INSERT);
        startActivity(intent);
    }

    private void loadData() {
        getLoaderManager().initLoader(LOADER_ASSET_CLASSES, null, this);
    }

}
