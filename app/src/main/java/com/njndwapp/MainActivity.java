package com.njndwapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.easemob.custommessage.PermissionChecker;
import com.easemob.custommessage.view.UpperPopupWindow;
import com.njndwapp.LiveAnchorActivity;
import com.njndwapp.LiveAudienceActivity;
import com.tencent.bugly.crashreport.CrashReport;


public class MainActivity extends Activity  implements View.OnClickListener {
    Button btnStart,btnInto;
    private PermissionChecker mPermissionChecker = new PermissionChecker(this);
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mPermissionChecker.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CrashReport.initCrashReport(this, "c5af42187f", true);
        setContentView(R.layout.em_activity_splash_type);
        btnStart=findViewById(R.id.btn_start);
        btnInto=findViewById(R.id.btn_into);
        btnStart.setOnClickListener(this);
        btnInto.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.btn_start: {
//                showPayPopupWindow();
                intent = new Intent(this, LiveAnchorActivity.class);
                intent.putExtra("publishUrl", "rtmp://pili-publish.yksbj.com/piliyikeshi/course_120829352411137_1_57b82ee3c4ec410e97bf7a3da237a1c3?e=1594633850&token=4kJKhYCmzbCJQCdUTIrkzmsiC9uFwCU_K3ac4oeB:pbqCqzrMP-0_Kk9p1Zb9_UlLgDw=");
                intent.putExtra("useName", "15722920744");
                intent.putExtra("passWord", "123456");
                intent.putExtra("uuid", "57b82ee3c4ec410e97bf7a3da237a1c3");//14805de4618948598be19b170e7d0626
                intent.putExtra("chatId", "120829352411137");
                intent.putExtra("cookie", "JSESSIONID=e82a254b-f23b-47cc-b0fb-c70cb70e2aa7");
                intent.putExtra("userNick","流量在拉萨");
                intent.putExtra("headFileId","/logic/sysfile/downloadImg/1267");
                startActivity(intent);
                break;
              }
              //{ NativeMap: {"headFileId":"/logic/sysfile/downloadImg/1267","userNick":"流量在拉萨","useName":"15722920744","publishUrl":"rtmp://pili-publish.yksbj.com/piliyikeshi/course_120829352411137_1_57b82ee3c4ec410e97bf7a3da237a1c3?e=1594633850&token=4kJKhYCmzbCJQCdUTIrkzmsiC9uFwCU_K3ac4oeB:pbqCqzrMP-0_Kk9p1Zb9_UlLgDw=","cookie":"JSESSIONID=e82a254b-f23b-47cc-b0fb-c70cb70e2aa7","uuid":"57b82ee3c4ec410e97bf7a3da237a1c3","chatId":"120829352411137"} }
              //{ NativeMap: {"headFileId":"/logic/sysfile/downloadImg/1267","userNick":"流量在拉萨","useName":"15722920744","publishUrl":"rtmp://pili-publish.yksbj.com/piliyikeshi/course_120755220185089_1_e36094f4c54849f9bc30438a06a4ed23?e=1594563152&token=4kJKhYCmzbCJQCdUTIrkzmsiC9uFwCU_K3ac4oeB:S8_Z0pG-0viht5jBhCyIRnHlVg0=","cookie":"JSESSIONID=555151e2-b954-4683-a43b-cf981cb383a7","uuid":"e36094f4c54849f9bc30438a06a4ed23","chatId":"120755220185089"} }
              //{ NativeMap: {"headFileId":"/logic/sysfile/downloadImg/1076","userNick":"student","useName":"19941539209","publishUrl":"rtmp://pili-publish.yksbj.com/piliyikeshi/course_118484511031297_1_8bd706e326504637b69d19fe4ab9d880?e=1592397635&token=4kJKhYCmzbCJQCdUTIrkzmsiC9uFwCU_K3ac4oeB:jFpxlw41Xef0dhtS5hygYt590JI=","cookie":"JSESSIONID=a38eff20-55e1-4c60-bc22-f607b291a7dd","uuid":"8bd706e326504637b69d19fe4ab9d880","chatId":"118484511031297"} }
              //{ NativeMap: {"headFileId":"/logic/sysfile/downloadImg/1076","userNick":"student","useName":"19941539209","publishUrl":"rtmp://pili-publish.yksbj.com/piliyikeshi/course_118043057389569_1_d019b8561fa245b1b2c4f3fbb1e38423?e=1591976632&token=4kJKhYCmzbCJQCdUTIrkzmsiC9uFwCU_K3ac4oeB:d0k6fA4mretzA859h9nNpUr3Tw0=","cookie":"JSESSIONID=52e6d8b0-3dd2-49c4-af1d-f64b9df7ada6","uuid":"d019b8561fa245b1b2c4f3fbb1e38423","chatId":"118043057389569"} }
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
                intent.putExtra("useName","19941539209");
                intent.putExtra("passWord","123456");
                intent.putExtra("uuid","8bd706e326504637b69d19fe4ab9d880");
                intent.putExtra("chatId","118484511031297");
                intent.putExtra("cookie","JSESSIONID=a38eff20-55e1-4c60-bc22-f607b291a7dd");
                intent.putExtra("headFileId","/logic/sysfile/downloadImg/1076");
                intent.putExtra("userNick","student");//18900000000
                startActivity(intent);
                break;
                default:break;
        }
    }
}
