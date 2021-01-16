package cn.edu.hebtu.software.zhilvdemo.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.Adapter.MessageCommentAdapter;
import cn.edu.hebtu.software.zhilvdemo.Adapter.MyAttentionListAdapter;
import cn.edu.hebtu.software.zhilvdemo.R;

/**
 * @ProjectName:    ZhiLv
 * @Description:    消息界面
 * @Author:         张璐婷
 * @CreateDate:     2020/12/14 19:48
 * @Version:        1.0
 */
public class MessageFragment extends Fragment {
    private RelativeLayout notification;
    private RelativeLayout good;
    private RelativeLayout chat;
    private View view;
    private PopupWindow popupWindow;
    private List<String> mDatas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container,false);
        initData();
        initView();
        registListener();
        return view;
    }

    private void initView(){
//        ListView listView = view.findViewById(R.id.message_commend_list);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                if(myComments.get(position).getReadFlag() == 0) {
////                    setRead(myComments.get(position).getId());
////                }
//                showPopupWindow(parent);
//
//            }
//        });
        RecyclerView recyclerView = view.findViewById(R.id.message_commend_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        MessageCommentAdapter adapter = new MessageCommentAdapter(mDatas, R.layout.item_mycomment, getActivity());
        adapter.setOnItemClickListener(new MessageCommentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                if(myComments.get(position).getReadFlag() == 0) {
//                    setRead(myComments.get(position).getId());
//                }
//                showPopupWindow();
            }
        });
        recyclerView.setAdapter(adapter);
    }


    private void registListener(){
        CustomOnClickListener listener = new CustomOnClickListener();
        notification = view.findViewById(R.id.message_rl_notification);
        good = view.findViewById(R.id.message_rl_good);
        chat = view.findViewById(R.id.message_rl_chat);
        notification.setOnClickListener(listener);
        good.setOnClickListener(listener);
        chat.setOnClickListener(listener);
    }

//    private void registPopupListener(View popupView){
//        CustomOnClickListener listener = new CustomOnClickListener();
//        Button reply = popupView.findViewById(R.id.reply);
//        Button delete = popupView.findViewById(R.id.delete);
//        Button detail = popupView.findViewById(R.id.detail);
//        reply.setOnClickListener(listener);
//        delete.setOnClickListener(listener);
//        detail.setOnClickListener(listener);
//    }

    class CustomOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.message_rl_notification:
                    break;
                case R.id.message_rl_good:
                    break;
                case R.id.message_rl_chat:
                    break;
                case R.id.reply:
                    break;
                case R.id.delete:
                    break;
                case R.id.detail:
                    break;
            }
        }
    }


    private void initData(){
        mDatas = new ArrayList<>();
        for(int i = 0;i < 10; ++i){
            mDatas.add("comment-->" + i);
        }
    }

}
