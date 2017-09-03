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
                .setCover(R.drawable.about_cover)
                .setBackgroundColor(R.color.beigeColor)
                .setName("Brain Collaboration")
                .setSubTitle("Android development")
                .setBrief("Расширь свой кругозор!")
                .setAppIcon(R.drawable.app_ico)
                .setAppName(R.string.app_name)
                .addGitHubLink("oZBo/Wordus")
                .addEmailLink("brain.collaboration@gmail.com")
                .addSkypeLink("krot9ka")
                .addLinkedInLink("eandreychenko")
                .addGooglePlusLink("109034441452721761842")
                .addFiveStarsAction()
                .setVersionNameAsAppSubTitle()
                .addShareAction(R.string.app_name)
                .setWrapScrollView(true)
                .setLinksAnimated(true)
                .setShowAsCard(true)
                .build();

        addContentView(view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
