package mo.com.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    private ParallaxListview plv;
    private ImageView iv_head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        plv = (ParallaxListview) findViewById(R.id.plv);

        //添加一个头部据
        View inflate = View.inflate(this, R.layout.header, null);
        plv.addHeaderView(inflate);

        iv_head = inflate.findViewById(R.id.iv_heade);

        //页面绘制完毕,去掉绘制完控件的宽和高,

        iv_head.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //宽高测量完毕
                plv.setparallaxImage(iv_head);
                //释放资源
                iv_head.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        //使用listview的arrayafapter,添加文本item
        plv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,cheeses.NAMES));
    }
}
