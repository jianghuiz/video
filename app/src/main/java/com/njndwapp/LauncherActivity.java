package com.njndwapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LauncherActivity extends Activity implements View.OnClickListener{
    Button btnStart,btnInto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_splash_type);
        btnStart = findViewById(R.id.btn_start);
        btnInto = findViewById(R.id.btn_into);
        btnStart.setOnClickListener(this);
        btnInto.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.btn_start: {
                intent = new Intent(this, LiveAnchorActivity.class);
                intent.putExtra("publishUrl", "rtmp://pili-publish.yksbj.com/piliyikeshi/course_117755157217281_1_1899ca54e82f43fd93e7326a97211366?e=1591702070&token=4kJKhYCmzbCJQCdUTIrkzmsiC9uFwCU_K3ac4oeB:698OgsVm4xd834piLUyr9bUlGj8=");
                intent.putExtra("useName", "15722920744");
                intent.putExtra("passWord", "123456");
                intent.putExtra("uuid", "1899ca54e82f43fd93e7326a97211366");//14805de4618948598be19b170e7d0626
                intent.putExtra("chatId", "117755157217281");
                intent.putExtra("cookie", "JSESSIONID=d96cb66c-a4e5-49cb-ad62-9b96efaedd0a");
                intent.putExtra("userNick","流量在拉萨");
                intent.putExtra("headFileId","/logic/sysfile/downloadImg/1069");
                startActivity(intent);
                break;
            }
            //{ NativeMap: {"headFileId":"/logic/sysfile/downloadImg/1069","userNick":"流量在拉萨","useName":"15722920744","publishUrl":"rtmp://pili-publish.yksbj.com/piliyikeshi/course_117755157217281_1_1899ca54e82f43fd93e7326a97211366?e=1591702070&token=4kJKhYCmzbCJQCdUTIrkzmsiC9uFwCU_K3ac4oeB:698OgsVm4xd834piLUyr9bUlGj8=","cookie":"JSESSIONID=d96cb66c-a4e5-49cb-ad62-9b96efaedd0a","uuid":"1899ca54e82f43fd93e7326a97211366","chatId":"117755157217281"} }
            //{ NativeMap: {"headFileId":"/logic/sysfile/downloadImg/1069","userNick":"流量在拉萨","useName":"15722920744","publishUrl":"rtmp://pili-publish.yksbj.com/piliyikeshi/course_117726551015426_1_0e3ca36085784bc6bb3a94533db8ed95?e=1591674789&token=4kJKhYCmzbCJQCdUTIrkzmsiC9uFwCU_K3ac4oeB:ek-tMfYfQTGw_15OaDp5TzZwcbo=","cookie":"JSESSIONID=975dc742-310d-40f4-931e-93bf4b7ef0d4","uuid":"0e3ca36085784bc6bb3a94533db8ed95","chatId":"117726551015426"} }
            //{ NativeMap: {"headFileId":"/logic/sysfile/downloadImg/1069","userNick":"流量在拉萨","useName":"15722920744","publishUrl":"rtmp://pili-publish.yksbj.com/piliyikeshi/course_117726551015426_1_0e3ca36085784bc6bb3a94533db8ed95?e=1591674789&token=4kJKhYCmzbCJQCdUTIrkzmsiC9uFwCU_K3ac4oeB:ek-tMfYfQTGw_15OaDp5TzZwcbo=","cookie":"JSESSIONID=975dc742-310d-40f4-931e-93bf4b7ef0d4","uuid":"0e3ca36085784bc6bb3a94533db8ed95","chatId":"117726551015426"} }
            //{ NativeMap: {"headFileId":"/logic/sysfile/downloadImg/1069","userNick":"流量在拉萨","useName":"15722920744","publishUrl":"rtmp://pili-publish.yksbj.com/piliyikeshi/course_117672096366594_1_0e3ca36085784bc6bb3a94533db8ed95?e=1591622857&token=4kJKhYCmzbCJQCdUTIrkzmsiC9uFwCU_K3ac4oeB:JFh-05v6PdfBQr1TBnpI74wnn_k=","cookie":"JSESSIONID=975dc742-310d-40f4-931e-93bf4b7ef0d4","uuid":"0e3ca36085784bc6bb3a94533db8ed95","chatId":"117672096366594"} }
            //{ NativeMap: {"userNick":"流量在拉萨","nickName":"流量在拉萨","headFileId":"/logic/sysfile/downloadImg/1069","useName":"15722920744","pullUrl":"rtmp://pili-live-rtmp.yksbj.com/piliyikeshi/course_117579380228097_1_4afc6825ff254a31a27c526c00c5f9e9","uuid":"4afc6825ff254a31a27c526c00c5f9e9","chatId":"117579380228097","cookie":"JSESSIONID=4c73912b-92a8-4f99-83b0-1430b3461563"} }
            //{ NativeMap: {"headFileId":"/logic/sysfile/downloadImg/1069","userNick":"流量在拉萨","useName":"15722920744","publishUrl":"rtmp://pili-publish.yksbj.com/piliyikeshi/course_117579380228097_1_4afc6825ff254a31a27c526c00c5f9e9?e=1591534435&token=4kJKhYCmzbCJQCdUTIrkzmsiC9uFwCU_K3ac4oeB:RqSnL-xwGefs60FSczaIZQjMhZ4=","cookie":"JSESSIONID=4c73912b-92a8-4f99-83b0-1430b3461563","uuid":"4afc6825ff254a31a27c526c00c5f9e9","chatId":"117579380228097"} }
            //{ NativeMap: {"headFileId":"/logic/sysfile/downloadImg/1069","userNick":"流量在拉萨","useName":"15722920744","publishUrl":"rtmp://pili-publish.yksbj.com/piliyikeshi/course_117564232499201_1_4afc6825ff254a31a27c526c00c5f9e9?e=1591519990&token=4kJKhYCmzbCJQCdUTIrkzmsiC9uFwCU_K3ac4oeB:uWJD2SCNTyngIC9id7bptctT1os=","cookie":"JSESSIONID=97789812-6b64-4aeb-b826-cdd640e6e3d5","uuid":"4afc6825ff254a31a27c526c00c5f9e9","chatId":"117564232499201"} }
            //{ NativeMap: {"headFileId":"/logic/sysfile/downloadImg/1069","userNick":"流量在拉萨","useName":"15722920744","publishUrl":"rtmp://pili-publish.yksbj.com/piliyikeshi/course_117480108392449_1_9aea08d7a98e41e692646eddfcd14099?e=1591439762&token=4kJKhYCmzbCJQCdUTIrkzmsiC9uFwCU_K3ac4oeB:IwvYBkNJ8G3WLn-0rGU1yUSyRq8=","cookie":"JSESSIONID=775473c1-6920-42d4-96ee-9a63973d2cf9","uuid":"9aea08d7a98e41e692646eddfcd14099","chatId":"117480108392449"} }

//            { NativeMap: {"headFileId":"/logic/sysfile/downloadImg/1069","userNick":"流量在拉萨","useName":"15722920744","publishUrl":"rtmp://pili-publish.yksbj.com/piliyikeshi/course_117478345736193_1_d1a0cab977ab40599171f258a69f78c7?e=1591438081&token=4kJKhYCmzbCJQCdUTIrkzmsiC9uFwCU_K3ac4oeB:TtV4Qgfi9DZQBizrjMg3z1o63mw=","cookie":"JSESSIONID=087fd3ef-40d5-4fc6-bd67-c5af15409376","uuid":"d1a0cab977ab40599171f258a69f78c7","chatId":"117478345736193"} }
            case R.id.btn_into:
                intent=new Intent(this, LiveAudienceActivity.class);
                intent.putExtra("pullUrl","rtmp://pili-live-rtmp.yksbj.com/piliyikeshi/course_117579380228097_1_4afc6825ff254a31a27c526c00c5f9e9");
                intent.putExtra("useName","000002");
                intent.putExtra("passWord","123456");
                intent.putExtra("uuid","1899ca54e82f43fd93e7326a97211366");
                intent.putExtra("chatId","117755157217281");
                intent.putExtra("cookie","JSESSIONID=d96cb66c-a4e5-49cb-ad62-9b96efaedd0a");
                intent.putExtra("headFileId","/logic/sysfile/downloadImg/1131");
                intent.putExtra("userNick","000002");//18900000000
                startActivity(intent);
                break;
            default:break;
        }
    }
}
