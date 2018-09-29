package com.example.ezzati.hidencam;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ListView LV;
    MainActivity m = this;
    private LayoutInflater mInflater;
    private File[] mPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mInflater = LayoutInflater.from(this);
        this.LV = (ListView) findViewById(R.id.HidenPhoto);

        Button Take =(Button)findViewById(R.id.TakePh);
        Take.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(m,CameraService.class);
                m.startService(i);

                updatePhotos();
            }
        });

    }

    protected void onResume() {
        super.onResume();
        updatePhotos();
    }




    private void updatePhotos() {

        new AsyncTask<Void, Void, Void>() {

            private FilenameFilter mFilter = new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return s.endsWith(".jpg");
                }
            };

            @Override
            protected Void doInBackground(Void... voids) {
                File photosDir = CameraManager.getDir();
                if(photosDir.exists() && photosDir.isDirectory())
                    mPhotos = photosDir.listFiles(mFilter);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                PhotoAdapter adapter = new PhotoAdapter();
                LV.setAdapter(adapter);
            }

        }.execute();

    }

    class PhotoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mPhotos != null ? mPhotos.length : 0;
        }

        @Override
        public Object getItem(int i) {
            return mPhotos != null ? mPhotos[i] : mPhotos;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View root = mInflater.inflate(R.layout.item_photo, viewGroup, false);
            ImageView photo = (ImageView) root.findViewById(R.id.photo_image);
            TextView text = (TextView) root.findViewById(R.id.photo_text);
            File f = (File) getItem(i);
            if(f != null) {
                photo.setImageURI(Uri.parse("file://" + f.getPath()));
                String filename = f.getName();
                String label = null;
                filename = filename.replace("spyapp_", "");
                filename = filename.replace(".jpg", "");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
                try {
                    Date photoDate = sdf.parse(filename);
                    sdf.applyPattern("hh:mm aa\nMMMM, dd, yyyy");
                    label = sdf.format(photoDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                text.setText(label != null ? label : filename);
            }
            return root;
        }
    }

}

