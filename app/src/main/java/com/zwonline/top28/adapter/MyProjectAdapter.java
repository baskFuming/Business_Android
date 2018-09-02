package com.zwonline.top28.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zwonline.top28.R;
import com.zwonline.top28.bean.ProjectBean;
import com.zwonline.top28.utils.ImageViewPlus;

import java.util.List;

/**我的项目列表
 * Created by sdh on 2018/3/8.
 */

public class MyProjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public MyProjectAdapter.OnClickItemListener onClickItemListener;
    private List<ProjectBean.DataBean> list;
    private Context context;

    public MyProjectAdapter(List<ProjectBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.project_item, parent, false);
        final ProjectViewHolder holder = new ProjectViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemListener.setOnItemClick(holder.getPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProjectViewHolder myViewHolder = (ProjectViewHolder) holder;
        ProjectBean.DataBean  projectBean = list.get(position);
        Glide.with(context).load(projectBean.logo).into(myViewHolder.projectImagHead);
        myViewHolder.projectName.setText(projectBean.enterprise_name);
        String projectScore = projectBean.score;
        myViewHolder.projectScore.setText( context.getString( R.string.project_credit_score," "+projectScore));
        if (projectBean.is_default.equals("1")&&projectBean.check_status.equals("1")){
            myViewHolder.certificateStatus.setText("已审核");
            myViewHolder.projectRelative.setBackgroundResource(R.drawable.rectangle_shape_pink);
        }else if (projectBean.is_default.equals("0")&&projectBean.check_status.equals("1")){
            myViewHolder.certificateStatus.setText("已审核");
//            myViewHolder.projectRelative.setBackgroundResource(R.drawable.btn_guanzhu_shape);
        }else if (projectBean.check_status.equals("2")){
            myViewHolder.certificateStatus.setText("未通过");
//            myViewHolder.projectRelative.setBackgroundResource(R.drawable.btn_guanzhu_shape);
        }else if (projectBean.check_status.equals("3")){
            myViewHolder.certificateStatus.setText("注销");
//            myViewHolder.projectRelative.setBackgroundResource(R.drawable.btn_guanzhu_shape);
        }else if (projectBean.check_status.equals("0")){
            myViewHolder.certificateStatus.setText("审核中");
//            myViewHolder.projectRelative.setBackgroundResource(R.drawable.btn_guanzhu_shape);
        }

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void setOnClickItemListener(MyProjectAdapter.OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener{
        void setOnItemClick(int position);
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        ImageViewPlus projectImagHead;
        TextView projectName, projectScore,certificateStatus;
        RelativeLayout projectRelative;

        public ProjectViewHolder(View itemView) {
            super(itemView);
            projectImagHead = (ImageViewPlus) itemView.findViewById(R.id.project_imag_head);
            projectName=(TextView)itemView.findViewById(R.id.project_name);
            projectScore=(TextView)itemView.findViewById(R.id.project_score);
            certificateStatus=(TextView)itemView.findViewById(R.id.certificate_status);
            projectRelative=(RelativeLayout)itemView.findViewById(R.id.project_relative);
        }
    }

}
