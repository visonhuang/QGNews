package com.qg.qgnews.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.ManagerPersonAdapter;
import com.qg.qgnews.model.FeedBack;
import com.qg.qgnews.model.Manager;
import com.qg.qgnews.model.RequestAdress;
import com.qg.qgnews.model.Status;
import com.qg.qgnews.ui.activity.ManagerNews;
import com.qg.qgnews.util.Request;
import com.qg.qgnews.util.Tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.icu.text.DateTimePatternGenerator.PatternInfo.OK;

/**
 * Created by linzongzhan on 2017/7/30.
 */

public class ManagerPerson extends Fragment {

    private static final int OK = 1;

    private View view;

    private ListView listView;

    private List<Manager> managers = new ArrayList<>();

    private SwipeRefreshLayout refreshLayout;

    private Handler handler = new Handler() {
        public void handleMessage (Message msg) {
            switch (msg.what) {
                case OK :
                    //刷新ListView（所有管理员）
                    ManagerPersonAdapter managerPersonAdapter = new ManagerPersonAdapter(getContext(),R.layout.manager_item,managers);
                    listView.setAdapter(managerPersonAdapter);
                    refreshLayout.setRefreshing(false);
                    break;
                default:
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_manager_person,container,false);

        listView = (ListView) view.findViewById(R.id.manager_person_listView);




   /*     Manager manager = new Manager();
        manager.setManagerName("6666");
        manager.setManagerStatus("待审批");
        for (int i = 0;i < 10;i++) {
            managers.add(manager);
        }
        ManagerPersonAdapter managerPersonAdapter = new ManagerPersonAdapter(getContext(),R.layout.manager_item,managers);
        listView.setAdapter(managerPersonAdapter);  */




        setViewOnClick();

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.manager_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //接受数据
                        String respose = Request.RequestWithNoString(RequestAdress.GET_ALL_MANAGER);
                        Gson gson = new Gson();
                        FeedBack feedBack = gson.fromJson(respose,FeedBack.class);
                        if (feedBack == null) {
                            Tool.toast("服务器无返回");
                            Tool.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refreshLayout.setRefreshing(false);
                                }
                            });
                        } else {
                            String data = feedBack.getData();
                            managers = gson.fromJson(data,new TypeToken<List<Manager>>(){}.getType());

                            Message message = new Message();
                            message.what = OK;
                            handler.sendMessage(message);
                        }


                    }
                }).start();
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                //接受数据
                String respose = Request.RequestWithNoString(RequestAdress.GET_ALL_MANAGER);
                Gson gson = new Gson();
                FeedBack feedBack = gson.fromJson(respose,FeedBack.class);
                if (feedBack == null) {
                    Tool.toast("服务器无返回");
                } else {
                    String data = feedBack.getData();
                    managers = gson.fromJson(data,new TypeToken<List<Manager>>(){}.getType());

                    Message message = new Message();
                    message.what = OK;
                    handler.sendMessage(message);
                }

            }
        }).start();

        return view;
    }

    private void setViewOnClick () {

        //点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Manager manager = managers.get(i);
                String managerStatus = manager.getManagerStatus();
                String name = manager.getManagerName();
                String account = manager.getManagerAccount();
                String password = manager.getManagerPassword();
                final int id = manager.getManagerId();

                if (managerStatus.equals("未审批")) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("审批中");
                    alert.setMessage("姓名：" + name + "\n" + "账号：" + account + "\n" + "密码：" + password);
                    alert.setPositiveButton("同意", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            //发送同意请求
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Gson gson = new Gson();
                                    Map<String,Integer> map = new HashMap<String, Integer>();
                                    map.put("managerId",id);
                                    String line = gson.toJson(map);
                                    String reponse = Request.RequestWithString(RequestAdress.AGRESS_APPLY,line);
                                    FeedBack feedBack = gson.fromJson(reponse,FeedBack.class);
                                    if (feedBack == null) {
                                        Tool.toast("服务器无返回");
                                    } else {
                                        if (feedBack.getState() == 1) {
                                            Tool.toast("已通过审批");
                                        }
                                        Status.statusResponse(feedBack.getState());
                                    }
                                 }
                            }).start();

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //接受数据
                                    try {
                                        Thread.sleep(500);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    String respose = Request.RequestWithNoString(RequestAdress.GET_ALL_MANAGER);
                                    Gson gson = new Gson();
                                    FeedBack feedBack = gson.fromJson(respose,FeedBack.class);
                                    if (feedBack == null) {
                                        Tool.toast("服务器无返回");
                                    } else {
                                        String data = feedBack.getData();
                                        managers = gson.fromJson(data,new TypeToken<List<Manager>>(){}.getType());

                                        Message message = new Message();
                                        message.what = OK;
                                        handler.sendMessage(message);
                                    }

                                }
                            }).start();


                        }
                    });
                    alert.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            //发送拒绝请求
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Gson gson = new Gson();
                                    Map<String,Integer> map = new HashMap<String, Integer>();
                                    map.put("managerId",id);
                                    String line = gson.toJson(map);
                                    String reponse = Request.RequestWithString(RequestAdress.DISAGRESS_APPLY,line);
                                    FeedBack feedBack = gson.fromJson(reponse,FeedBack.class);
                                    if (feedBack == null) {
                                        Tool.toast("服务器无返回");
                                    } else {
                                        if (feedBack.getState() == 1) {
                                            Tool.toast("已拒绝");
                                        }
                                        Status.statusResponse(feedBack.getState());
                                    }
                                }
                            }).start();

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //接受数据
                                    try {
                                        Thread.sleep(500);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    String respose = Request.RequestWithNoString(RequestAdress.GET_ALL_MANAGER);
                                    Gson gson = new Gson();
                                    FeedBack feedBack = gson.fromJson(respose,FeedBack.class);
                                    if (feedBack == null) {
                                        Tool.toast("服务器无返回");
                                    } else {
                                        String data = feedBack.getData();
                                        managers = gson.fromJson(data,new TypeToken<List<Manager>>(){}.getType());

                                        Message message = new Message();
                                        message.what = OK;
                                        handler.sendMessage(message);
                                    }

                                }
                            }).start();


                        }
                    });
                    alert.create().show();
                }

                if (managerStatus.equals("正常")) {
                    //发送获得此管理员数据的请求
                    Intent intent = new Intent(getContext(), ManagerNews.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }

                if (managerStatus.equals("被封号")) {
                    //发送获得此管理员数据的请求
                    Intent intent = new Intent(getContext(), ManagerNews.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }

            }
        });

        //长按事件
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Manager manager = managers.get(i);
                String managerStatus = manager.getManagerStatus();
                final String name = manager.getManagerName();
                final String account = manager.getManagerAccount();
                final String password = manager.getManagerPassword();
                final int id = manager.getManagerId();

                if (managerStatus.equals("未审批")) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("审批中");
                    alert.setMessage("姓名：" + name + "\n" + "账号：" + account + "\n" + "密码：" + password);
                    alert.setPositiveButton("同意", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            //发送同意请求
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Gson gson = new Gson();
                                    Map<String,Integer> map = new HashMap<String, Integer>();
                                    map.put("managerId",id);
                                    String line = gson.toJson(map);
                                    String reponse = Request.RequestWithString(RequestAdress.AGRESS_APPLY,line);
                                    FeedBack feedBack = gson.fromJson(reponse,FeedBack.class);
                                    if (feedBack == null) {
                                        Tool.toast("服务器无返回");
                                    } else {
                                        if (feedBack.getState() == 1) {
                                            Tool.toast("已通过审批");
                                        }
                                        Status.statusResponse(feedBack.getState());
                                    }
                                }
                            }).start();

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //接受数据
                                    try {
                                        Thread.sleep(500);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    String respose = Request.RequestWithNoString(RequestAdress.GET_ALL_MANAGER);
                                    Gson gson = new Gson();
                                    FeedBack feedBack = gson.fromJson(respose,FeedBack.class);
                                    if (feedBack == null) {
                                        Tool.toast("服务器无返回");
                                    } else {
                                        String data = feedBack.getData();
                                        managers = gson.fromJson(data,new TypeToken<List<Manager>>(){}.getType());

                                        Message message = new Message();
                                        message.what = OK;
                                        handler.sendMessage(message);
                                    }

                                }
                            }).start();


                        }
                    });
                    alert.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            //发送拒绝请求
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Gson gson = new Gson();
                                    Map<String,Integer> map = new HashMap<String, Integer>();
                                    map.put("managerId",id);
                                    String line = gson.toJson(map);
                                    String reponse = Request.RequestWithString(RequestAdress.DISAGRESS_APPLY,line);
                                    FeedBack feedBack = gson.fromJson(reponse,FeedBack.class);
                                    if (feedBack == null) {
                                        Tool.toast("服务器无返回");
                                    } else {
                                        if (feedBack.getState() == 1) {
                                            Tool.toast("已拒绝");
                                        }
                                        Status.statusResponse(feedBack.getState());
                                    }
                                }
                            }).start();

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //接受数据
                                    try {
                                        Thread.sleep(500);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    String respose = Request.RequestWithNoString(RequestAdress.GET_ALL_MANAGER);
                                    Gson gson = new Gson();
                                    FeedBack feedBack = gson.fromJson(respose,FeedBack.class);
                                    if (feedBack == null) {
                                        Tool.toast("服务器无返回");
                                    } else {
                                        String data = feedBack.getData();
                                        managers = gson.fromJson(data,new TypeToken<List<Manager>>(){}.getType());

                                        Message message = new Message();
                                        message.what = OK;
                                        handler.sendMessage(message);
                                    }

                                }
                            }).start();


                        }
                    });
                    alert.create().show();
                }

                if (managerStatus.equals("正常")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("管理员选项");
                    final String[] choose = {"查看详细信息","封号"};
                    builder.setItems(choose, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (i == 0) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                alert.setTitle("管理员信息");
                                alert.setMessage("姓名：" + name + "\n" + "账号：" + account + "\n" + "密码：" + password);
                                alert.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                alert.show();
                            }
                            if (i == 1) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                alert.setTitle("提示");
                                alert.setMessage("是否将管理员 " + name + " 封号");
                                alert.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        //封号请求
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Gson gson = new Gson();
                                                Map<String,Integer> map = new HashMap<String, Integer>();
                                                map.put("managerId",id);
                                                String line = gson.toJson(map);
                                                String response = Request.RequestWithString(RequestAdress.RESTRICTACCOUNT,line);
                                                FeedBack feedBack = gson.fromJson(response,FeedBack.class);
                                                if (feedBack == null) {
                                                    Tool.toast("服务器无返回");
                                                } else {
                                                    if (feedBack.getState() == 1) {
                                                        Tool.toast("封号成功");
                                                    }
                                                    Status.statusResponse(feedBack.getState());
                                                }
                                            }
                                        }).start();

                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //接受数据
                                                try {
                                                    Thread.sleep(500);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                String respose = Request.RequestWithNoString(RequestAdress.GET_ALL_MANAGER);
                                                Gson gson = new Gson();
                                                FeedBack feedBack = gson.fromJson(respose,FeedBack.class);
                                                if (feedBack == null) {
                                                    Tool.toast("服务器无返回");
                                                } else {
                                                    String data = feedBack.getData();
                                                    managers = gson.fromJson(data,new TypeToken<List<Manager>>(){}.getType());

                                                    Message message = new Message();
                                                    message.what = OK;
                                                    handler.sendMessage(message);
                                                }

                                            }
                                        }).start();
                                    }
                                });
                                alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                alert.show();
                            }
                        }
                    });
                    builder.show();
                }


                return true;
            }
        });
    }
}
