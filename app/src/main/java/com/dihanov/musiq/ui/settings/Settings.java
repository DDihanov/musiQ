package com.dihanov.musiq.ui.settings;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import com.dihanov.musiq.R;
import com.dihanov.musiq.data.db.ScrobbleDB;
import com.dihanov.musiq.data.repository.scrobble.Scrobble;
import com.dihanov.musiq.data.repository.scrobble.ScrobbleRepository;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.service.MediaControllerListenerService;
import com.dihanov.musiq.ui.adapters.ScrobbleReviewAdapter;
import com.dihanov.musiq.util.Connectivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class Settings extends AppCompatPreferenceActivity implements HasFragmentInjector {
    public static final String PLAYER_PREFIX = "player_prefix.";

    @Override
    public AndroidInjector<Fragment> fragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || PlayerPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName)
                || ScrobbleReviewFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class PlayerPreferenceFragment extends PreferenceFragment {
        @Inject
        UserSettingsRepository userSettingsRepository;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            AndroidInjection.inject(this);
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            Map<String, ?> all = userSettingsRepository.getAll();

            PreferenceScreen root = getPreferenceScreen();
            PackageManager packageManager = root.getContext().getApplicationContext().getPackageManager();
            List<String> players = new ArrayList<>();
            for (String s : all.keySet()) {
                if(s.startsWith(MediaControllerListenerService.CONTROLLER_PREFIX)){
                    players.add(s.substring(17, s.length()));
                }
            }
            for (String key : players) {
                String name = key;
                Preference preference = new SwitchPreference(root.getContext());
                ApplicationInfo musicPlayer;

                try {
                    musicPlayer = packageManager.getApplicationInfo(name, 0);
                    preference.setTitle(packageManager.getApplicationLabel(musicPlayer));
                    preference.setIcon(packageManager.getApplicationIcon(musicPlayer));
                    preference.setKey(PLAYER_PREFIX + name);
                    preference.setDefaultValue(false);
                    root.addPreference(preference);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), Settings.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), Settings.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ScrobbleReviewFragment extends android.app.Fragment {
        @BindView(R.id.scrobble_review_list)
        ListView scrobbleListView;

        @BindView(R.id.scrobble_review_scrobble_all)
        MaterialButton scrobbleAll;

        @BindView(R.id.scrobble_review_empty)
        TextView empty;

        @Inject
        ScrobbleDB scrobbleDB;

        @Inject
        ScrobbleRepository scrobbleRepository;

        private List<Scrobble> cachedScrobbles;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            AndroidInjection.inject(this);
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.scrobble_review_fragment, container, false);
            ButterKnife.bind(this, view);

            return view;
        }

        private void init() {
            List<Scrobble> cachedScrobbles = scrobbleDB.getCachedScrobbles();
            this.cachedScrobbles = cachedScrobbles;

            if (this.cachedScrobbles.isEmpty()) {
                empty.setVisibility(View.VISIBLE);
                scrobbleAll.setVisibility(View.INVISIBLE);
                scrobbleListView.setVisibility(View.INVISIBLE);
            } else {
                empty.setVisibility(View.INVISIBLE);
            }

            scrobbleListView.setAdapter(new ScrobbleReviewAdapter(getActivity(), R.layout.scrobble_review_layout, this.cachedScrobbles, scrobbleRepository, scrobbleDB));

            scrobbleAll.setOnClickListener(v -> {
                if (!Connectivity.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                if (this.cachedScrobbles.size() >= 50) {
                    b.setTitle(getString(R.string.review_warning));
                    b.setMessage(R.string.review_messages);
                    b.setCancelable(true);
                    b.setNeutralButton(getString(R.string.dialog_action_dismiss), (dialog, which) -> dialog.dismiss());
                    b.create().show();
                } else {
                    b.setTitle(getString(R.string.scrobble_all_confirmation));
                    b.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            scrobbleRepository.scrobbleFromCacheDirectly();
                            ((ArrayAdapter<Scrobble>) scrobbleListView.getAdapter()).clear();
                            ((ArrayAdapter<Scrobble>) scrobbleListView.getAdapter()).notifyDataSetChanged();
                        }
                    });
                    b.setCancelable(true);
                    b.setNeutralButton(getString(R.string.dialog_action_dismiss), (dialog, which) -> dialog.dismiss());
                    b.create().show();
                }
            });
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), Settings.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onResume() {
            init();
            super.onResume();
        }
    }
}
