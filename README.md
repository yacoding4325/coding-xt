# coding-xt
# yacoding-xt-parent
##  开发项目的流程

后端程序员：

1. 产品经理
2. 前端
3. 测试

开发流程：

1. BRD，MRD
2. 产品经理 出 PRD文档 产品需求文档
3. 评审
4. 下发需求，会和后端程序员开会
5. 产品经理 出一个 原型图
6. 需求评估会议，前端，后端，UI，产品等共同参与，估时的工作，领任务的过程
   1. 估时 需要把测试的时间 估算进来
   2. 1h，0.5d，1d，2d，3d
7. 开发内部工作
8. 分配任务，估算时间
9. 项目设计文档  参数的定义，接口的说明，流程的说明，返回的数据说明，技术栈等等
10. 数据库设计阶段，技术leader，资深开发带领
11. 评审 设计文档 数据库设计
12. 开发人员 会搭建整体的项目，上传git，其他开发人员 拉取代码，开始在此框架上进行对应的开发
13. 需要给前端人员 提供一些 接口的定义（接口路径，请求方式，参数，返回结果等）
14. 编码阶段，开例会或者叫早会
15. 自测
16. 提交测试
17. 测试人员介入
18. 改bug
19. 测试人员 可以达到上线标准
20. 上线，部署线上的环境了，打包，数据库导入，自动化运维

## 项目背景

> 中小学生能取得好成绩的前提是，进行大量刷题，但是题必须有质量，一些名师和一些已经鸡娃结束的家长，联合起来，将一些有超高价值的题总结起来，其中不同城市对题的要求是不一样的。
>
> 需求开发一个在线的刷题平台，考虑到后期人数多了之后，可以请名师录制一些教学视频或者开设对应的直播课，所以本系统定义为综合性的在线教育平台。
>
> 这些题是以课程的形式售卖的，只有买了对应的课程，才能去做题或者学习视频教程，系统的初期是以刷题为主，习题分为填空题，单选，多选，判断，问答题，题目和选项均包含图片。
>
> 需要有分销商，帮忙卖课程，也就是涉及到分佣，优惠等，这些都需要考虑，同时不只要支持pc端，公众号端也需要支持，因为推广都是微信群推广，需要在微信端直接完成交易，锁定用户。



## 登录注册
### 1. 项目搭建

#### 1.1 项目结构
![image](https://user-images.githubusercontent.com/82166879/174314778-9ca23f60-0674-4ac1-a6a1-1c7f52c23a18.png)


## 新闻列表
### 1. 新闻列表

#### 1.1 需求
![image](https://user-images.githubusercontent.com/82166879/174415446-0e2f732e-5746-4f0a-a761-fb564319ed53.png)


网站首页会有相关的新闻资讯显示，点进去会有详情显示


#### 1.2 数据库设计


#### 1.3 步骤分析

1. 首页列表实现，每一个标签只显示5条最新的数据，查询条件中根据标签查询，每次切换标签都访问一次接口
2. 列表查询sql语句，只查询题目以及id，提高查询性能
3. 查询出结果后，放入redis缓存中，设置2分钟的过期时间，加快访问速度的同时也可以防止出现集中访问的情况，应对短暂的热点现象
4. 文章详情，根据id查询文章的详细信息，同样放入缓存中，考虑到文章内容过大，设计30秒的过期时间,后期如果出现访问量激增的情况，考虑放入es中
5. 列表详情页面，分页查询，同样放入缓存，sql语句只查询需要的信息即可
6. 可以写缓存的通用实现

#### 1.4 编码

#### 1.5 单点登录介绍

> 举个简单的例子，阿里巴巴旗下有淘宝网，飞猪网等，用于如果登录了淘宝网，在访问飞猪网的时候，还需要登录吗？
>
> 很明显不需要了，如果用户多次登录，体验性较差，对阿里技术团队来说，淘宝和飞猪都属于集团旗下的，用户表是一个，开发一次登录功能即可。
>
> 单点登录：简称为 SSO，SSO的定义是在多个应用系统中，用户只需要登录一次就可以访问所有相互信任的应用系统。

单点登录流程：
![单点登录](https://user-images.githubusercontent.com/82166879/190905056-2ce44a1f-1e9d-4c04-9116-871efd874cdd.png)


