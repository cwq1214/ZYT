package com.zyt.clp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.baidu.lbsapi.panoramaview.PanoramaViewListener;
import com.zyt.App;
import com.zyt.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class QJActivity extends AppCompatActivity {

    @InjectView(R.id.qjMapView)
    PanoramaView qjMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initQJView();

        setContentView(R.layout.activity_qj);
        ButterKnife.inject(this);

        qjMapView.setPanorama(getIntent().getDoubleExtra("longi",0),getIntent().getDoubleExtra("lanti",0));
        qjMapView.setPanoramaImageLevel(PanoramaView.ImageDefinition.ImageDefinitionHigh);
        qjMapView.setPanoramaViewListener(new PanoramaViewListener() {
            @Override
            public void onDescriptionLoadEnd(String s) {

            }

            @Override
            public void onLoadPanoramaBegin() {

            }

            @Override
            public void onLoadPanoramaEnd(String s) {

            }

            @Override
            public void onLoadPanoramaError(String s) {

            }

            @Override
            public void onMessage(String s, int i) {

            }

            @Override
            public void onCustomMarkerClick(String s) {

            }

            @Override
            public void onMoveStart() {

            }

            @Override
            public void onMoveEnd() {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        qjMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        qjMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        qjMapView.destroy();
        super.onDestroy();
    }

    private void initQJView(){
        App app = (App)this.getApplication();
        if (app.bMapManager == null){
            app.bMapManager = new BMapManager(app);
            app.bMapManager.init(new App.MyGeneralListener());
        }
    }

    @OnClick(R.id.backBtn)
    public void onClick() {
        finish();
    }
}
