/**
 * @package_name : com.example.BeaconTest
 * @file_name : GalleryAcitiviy.java
 * @date : 2014. 11. 7. 
 * @time : 오전 12:34:31
 * @author : JongHun Lee
 * @Contect :
 */
package com.example.beaconandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author Hoom
 */
public class GalleryAcitiviy extends Activity implements OnItemClickListener {

    private ListView m_ListView;
    private ArrayAdapter<String> m_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO 자동 생성된 메소드 스텁
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);
        m_Adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1);

        m_ListView = (ListView) findViewById(R.id.gallery);

        m_Adapter.add("제 1 전시관");
        m_Adapter.add("제 2 전시관");
        m_Adapter.add("제 3 전시관");
        m_Adapter.add("제 4 전시관");

        m_ListView.setAdapter(m_Adapter);
        m_ListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        System.exit(0);
    }
}
