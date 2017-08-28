package braincollaboration.wordus.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.vansuita.materialabout.builder.AboutBuilder;
import com.vansuita.materialabout.views.AboutView;

import braincollaboration.wordus.R;

public class AboutActivity extends Activity {
    // not working
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AboutView view = AboutBuilder.with(this)
                .setPhoto(R.drawable.profile_picture)
                .setCover(R.drawable.skulls)
                .setName("Brain Collaboration Games")
                .setSubTitle("Android разработчик")
                .setBrief("I'm warmed of mobile technologies. Ideas maker, curious and nature lover.")
                .setAppIcon(R.drawable.app_ico)
                .setAppName(R.string.app_name)
                .addGooglePlayStoreLink("8002078663318221363")
                .addGitHubLink("oZBo")
                //.addFacebookLink("user")
                .addFiveStarsAction()
                .setVersionNameAsAppSubTitle()
                .addShareAction(R.string.app_name)
                .setWrapScrollView(true)
                .setLinksAnimated(true)
                .setShowAsCard(true)
                .build();

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, -2);
        addContentView(view, layoutParams);
    }
}
