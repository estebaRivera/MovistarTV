package com.smartboxtv.movistartv.programation.menu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.activities.PreviewActivity;
import com.smartboxtv.movistartv.animation.ManagerAnimation;
import com.smartboxtv.movistartv.data.database.DataBase;
import com.smartboxtv.movistartv.data.database.Reminder;
import com.smartboxtv.movistartv.data.image.ScreenShot;
import com.smartboxtv.movistartv.data.models.Channel;
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.delgates.UpdateNotificationDelegate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Esteban- on 12-05-14.
 */
public class NotificationFragment extends Fragment {

    private List<Reminder> reminderList = new ArrayList<Reminder>();
    private List<Reminder> updateReminderList = new ArrayList<Reminder>();
    private View rootView;

    private UpdateNotificationDelegate notificationDelegate;

    public NotificationFragment() {
    }

    public void setNotificationDelegate(UpdateNotificationDelegate notificationDelegate) {
        this.notificationDelegate = notificationDelegate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.action_bar_notification, container, false);
        DataBase dataBase = new DataBase(getActivity(),"",null,1);
        TextView title = (TextView) rootView.findViewById(R.id.title_notification);
        Typeface normal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");
        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");
        Typeface light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Light.ttf");
        AQuery aq = new AQuery(rootView);
        title.setTypeface(bold);

        ImageView exit = (ImageView) rootView.findViewById(R.id.exit);
        RelativeLayout wrapper = (RelativeLayout) rootView.findViewById(R.id.wrapper);
        ImageView arrow = (ImageView) rootView.findViewById(R.id.notification_arrow);

        ManagerAnimation.fade(wrapper, arrow, exit);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageButton iconExit = (ImageButton) rootView.findViewById(R.id.icon_exit);

        iconExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(notificationDelegate!= null){
                    notificationDelegate.updateNotification(true);
                }
                finish();
            }
        });

        reminderList =  dataBase.getReminder();
        updateReminder();

        if(updateReminderList.size() > 0){
            RelativeLayout r = (RelativeLayout) rootView.findViewById(R.id.container_message);
            RelativeLayout n = (RelativeLayout) rootView.findViewById(R.id.no_notification);
            LinearLayout l = (LinearLayout) rootView.findViewById(R.id.list_notifications);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE HH:mm");
            LayoutInflater inf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            r.setVisibility(View.VISIBLE);
            n.setVisibility(View.GONE);
            Log.e("update","reminder +1");
            LinearLayout[] list = new LinearLayout [updateReminderList.size()];
            for(int i = 0 ; i < updateReminderList.size();i++){

                final int finalI = i;
                list[i] = new LinearLayout(getActivity());
                View notification = inf.inflate(R.layout.element_notification, null);

                TextView name = (TextView) notification.findViewById(R.id.txt_nombre_notification);
                TextView date = (TextView) notification.findViewById(R.id.txt_fecha_notification);

                ImageView image = (ImageView) notification.findViewById(R.id.img_notifications);

                if(updateReminderList.get(i).image != null)
                    aq.id(image).image(updateReminderList.get(i).image);
                date.setTypeface(light);
                name.setTypeface(bold);
                name.setText(updateReminderList.get(i).getName());
                date.setText(dateFormat.format(new Date(Long.parseLong(updateReminderList.get(i).getStrDate().replace(" ","")))));

                notification.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Program p = new Program();
                        p.EndDate = new Date(Long.parseLong(updateReminderList.get(finalI).endDate.replace(" ", "")));
                        p.StartDate = new Date(Long.parseLong(updateReminderList.get(finalI).strDate.replace(" ","")));
                        p.IdProgram = updateReminderList.get(finalI).id;
                        p.PChannel = new Channel();
                        p.PChannel.channelID =updateReminderList.get(finalI).idChannel;

                        rootView.setVisibility(View.INVISIBLE);
                        RelativeLayout r = (RelativeLayout) getActivity().findViewById(R.id.view_parent);
                        Bitmap screenShot = ScreenShot.takeScreenshot(r);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        screenShot.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                        byte[] byteArray = stream.toByteArray();

                        try {
                            String filename = getActivity().getCacheDir()
                                    + File.separator + System.currentTimeMillis() + ".jpg";

                            File f = new File(filename);
                            f.createNewFile();
                            FileOutputStream fo = new FileOutputStream(f);
                            fo.write(byteArray);
                            fo.close();

                            Intent intent = new Intent(getActivity(), PreviewActivity.class);
                            intent.putExtra("background", filename);
                            intent.putExtra("programa",p );
                            intent.putExtra("file",f);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.zoom_in_preview, R.anim.nada);
                            rootView.setVisibility(View.GONE);
                            onDestroy();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                list[i].addView(notification);
                l.addView(list[i]);
            }

        }
        return rootView;
    }
    public void finish(){

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(rootView, View.ALPHA,1,0);
        AnimatorSet animView = new AnimatorSet();

        animView.play(animatorX);
        animView.setDuration(400);

        animView.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                onDetach();
                onDestroy();
                RelativeLayout r = (RelativeLayout) getActivity().findViewById(R.id.contenedor_menu_bar);
                r.removeAllViews();

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animView.start();
    }

    public void updateReminder(){
        long inferior = new Date().getTime();
        long superior = inferior + 600000;
        long starDate;
        //Log.e("update","reminder");
        for (Reminder aReminderList : reminderList) {
            starDate = Long.parseLong(aReminderList.getStrDate().replace(" ", ""));
            if (starDate > inferior && starDate < superior) {
                updateReminderList.add(aReminderList);
            }
        }
    }
}
