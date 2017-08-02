package com.qg.qgnews.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
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
import com.qg.qgnews.controller.adapter.ManagerPersonEAdapter;
import com.qg.qgnews.model.FeedBack;
import com.qg.qgnews.model.Manager;
import com.qg.qgnews.model.RequestAdress;
import com.qg.qgnews.model.Status;
import com.qg.qgnews.util.Request;
import com.qg.qgnews.util.Tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.handle;
import static android.R.attr.widgetLayout;
import static android.content.ContentValues.TAG;

/**
 * Created by linzongzhan on 2017/7/30.
 */

public class ManagerPersonE extends Fragment {

    private static final int OK = 1;

    private View view;

    private ListView listView;

    private List<Manager> managers = new ArrayList<>();

    private SwipeRefreshLayout refreshLayout;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OK:
                    //刷新ListView（部分管理员）

                    ManagerPersonEAdapter managerPersonEAdapter = new ManagerPersonEAdapter(getContext(), R.layout.manager_item, managers);
                    listView.setAdapter(managerPersonEAdapter);
                    refreshLayout.setRefreshing(false);
                    break;
                default:
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_manager_person, container, false);


        listView = (ListView) view.findViewById(R.id.manager_person_listView);
        setViewOnclick();


  /*      Manager manager = new Manager();
        manager.setManagerName("6666");
        manager.setManagerStatus("待审批");
        for (int i = 0;i < 10;i++) {
            managers.add(manager);
        }

        ManagerPersonEAdapter managerPersonEAdapter = new ManagerPersonEAdapter(getContext(),R.layout.manager_item,managers);
        listView.setAdapter(managerPersonEAdapter);  */


        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.manager_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //接受数据
                        try {
                            String respose = Request.RequestWithNoString(RequestAdress.GET_ALL_MANAGER_NOT_PASS);
                            Gson gson = new Gson();
                            FeedBack feedBack = gson.fromJson(respose, FeedBack.class);
                            String data = feedBack.getData();
                            managers = gson.fromJson(data, new TypeToken<List<Manager>>() {
                            }.getType());

                            Message message = new Message();
                            message.what = OK;
                            handler.sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Tool.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    refreshLayout.setRefreshing(false);
                                }
                            });
                            Tool.toast("好像出了点问题");
                        }

                    }
                }).start();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                //接受数据
                String respose = Request.RequestWithNoString(RequestAdress.GET_ALL_MANAGER_NOT_PASS);
                Gson gson = new Gson();
                FeedBack feedBack = gson.fromJson(respose, FeedBack.class);
                if (feedBack == null) {
                    Tool.toast("服务器无返回");
                } else {
                    String data = feedBack.getData();
                    managers = gson.fromJson(data, new TypeToken<List<Manager>>() {
                    }.getType());

                    Message message = new Message();
                    message.what = OK;
                    handler.sendMessage(message);
                }

            }
        }).start();


        return view;
    }

    private void setViewOnclick() {

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
                                    Map<String, Integer> map = new HashMap<String, Integer>();
                                    map.put("managerId", id);
                                    String line = gson.toJson(map);
                                    FeedBack feedBack = Request.RequestWithString2(RequestAdress.AGRESS_APPLY, line);
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
                                    String respose = Request.RequestWithNoString(RequestAdress.GET_ALL_MANAGER_NOT_PASS);
                                    Gson gson = new Gson();
                                    FeedBack feedBack = gson.fromJson(respose, FeedBack.class);
                                    if (feedBack == null) {
                                        Tool.toast("服务器无返回");
                                    } else {
                                        String data = feedBack.getData();
                                        managers = gson.fromJson(data, new TypeToken<List<Manager>>() {
                                        }.getType());

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
                                    Map<String, Integer> map = new HashMap<String, Integer>();
                                    map.put("managerId", id);
                                    String line = gson.toJson(map);
                                    FeedBack feedBack = Request.RequestWithString2(RequestAdress.DISAGRESS_APPLY, line);
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
                                    String respose = Request.RequestWithNoString(RequestAdress.GET_ALL_MANAGER_NOT_PASS);
                                    Gson gson = new Gson();
                                    FeedBack feedBack = gson.fromJson(respose, FeedBack.class);
                                    if (feedBack == null) {
                                        Tool.toast("服务器无返回");
                                    } else {
                                        String data = feedBack.getData();
                                        managers = gson.fromJson(data, new TypeToken<List<Manager>>() {
                                        }.getType());

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
            }
        });

        //长按事件
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
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
                            //发送同意请求
                            alert.create().dismiss();

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Gson gson = new Gson();
                                    Map<String, Integer> map = new HashMap<String, Integer>();
                                    map.put("managerId", id);
                                    String line = gson.toJson(map);
                                    String reponse = Request.RequestWithString(RequestAdress.AGRESS_APPLY, line);
                                    FeedBack feedBack = gson.fromJson(reponse, FeedBack.class);
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
                                    String respose = Request.RequestWithNoString(RequestAdress.GET_ALL_MANAGER_NOT_PASS);
                                    Gson gson = new Gson();
                                    FeedBack feedBack = gson.fromJson(respose, FeedBack.class);
                                    if (feedBack == null) {
                                        Tool.toast("服务器无返回");
                                    } else {
                                        String data = feedBack.getData();
                                        managers = gson.fromJson(data, new TypeToken<List<Manager>>() {
                                        }.getType());

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
                                    Map<String, Integer> map = new HashMap<String, Integer>();
                                    map.put("managerId", id);
                                    String line = gson.toJson(map);
                                    String reponse = Request.RequestWithString(RequestAdress.DISAGRESS_APPLY, line);
                                    FeedBack feedBack = gson.fromJson(reponse, FeedBack.class);
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
                                    String respose = Request.RequestWithNoString(RequestAdress.GET_ALL_MANAGER_NOT_PASS);
                                    Gson gson = new Gson();
                                    FeedBack feedBack = gson.fromJson(respose, FeedBack.class);
                                    if (feedBack == null) {
                                        Tool.toast("服务器无返回");
                                    } else {
                                        String data = feedBack.getData();
                                        managers = gson.fromJson(data, new TypeToken<List<Manager>>() {
                                        }.getType());

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
                return true;
            }
        });
    }

}
