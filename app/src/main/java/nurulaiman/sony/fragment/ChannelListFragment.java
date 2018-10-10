/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package nurulaiman.sony.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.widget.GuidanceStylist.Guidance;
import android.support.v17.leanback.widget.GuidedAction;
import android.widget.Toast;

import java.util.List;

/**
 * TODO: Javadoc
 */
public class ChannelListFragment extends GuidedStepFragment {

    /*private static final int ACTION_ID_POSITIVE = 1;
    private static final int ACTION_ID_NEGATIVE = ACTION_ID_POSITIVE + 1;
*/
    private static final int ACTION_ID_CHANNEL1 = 1;
    private static final int ACTION_ID_CHANNEL2 = 2;
    private static final int ACTION_ID_CHANNEL3 = 3;
    private static final int ACTION_ID_CHANNEL4 = 4;
    private static final int ACTION_ID_CHANNEL5 = 5;

    @NonNull
    @Override
    public Guidance onCreateGuidance(Bundle savedInstanceState) {
        Guidance guidance = new Guidance(getString(R.string.dialog_channel_list_title),
                getString(R.string.dialog_channel_list_description),
                "", null);
        return guidance;
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction action = new GuidedAction.Builder()
                .id(ACTION_ID_CHANNEL1)
                .title(getString(R.string.dialog_example_button_channel1)).build();
        actions.add(action);

        action = new GuidedAction.Builder()
                .id(ACTION_ID_CHANNEL2)
                .title(getString(R.string.dialog_example_button_channel2)).build();
        actions.add(action);

        action = new GuidedAction.Builder()
                .id(ACTION_ID_CHANNEL3)
                .title(getString(R.string.dialog_example_button_channel3)).build();
        actions.add(action);

        action = new GuidedAction.Builder()
                .id(ACTION_ID_CHANNEL4)
                .title(getString(R.string.dialog_example_button_channel4)).build();
        actions.add(action);

        action = new GuidedAction.Builder()
                .id(ACTION_ID_CHANNEL5)
                .title(getString(R.string.dialog_example_button_channel5)).build();
        actions.add(action);
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        if (ACTION_ID_CHANNEL1 == action.getId()) {
            Toast.makeText(getActivity(), R.string.dialog_example_button_channel1 + " clicked",
                    Toast.LENGTH_SHORT).show();
        }
        else if (ACTION_ID_CHANNEL2 == action.getId()) {
            Toast.makeText(getActivity(), R.string.dialog_example_button_channel2 + " clicked",
                    Toast.LENGTH_SHORT).show();
        }
        else if (ACTION_ID_CHANNEL3 == action.getId()) {
            Toast.makeText(getActivity(), R.string.dialog_example_button_channel3 + " clicked",
                    Toast.LENGTH_SHORT).show();
        }
        else if (ACTION_ID_CHANNEL4 == action.getId()) {
            Toast.makeText(getActivity(), R.string.dialog_example_button_channel4 + " clicked",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(), R.string.dialog_example_button_channel5 + " clicked",
                    Toast.LENGTH_SHORT).show();
        }
        getActivity().finish();
    }
}
