ToodaylabReader
===============
理想生活实验室阅读器

##DONE
###2013-08-08
* 解析RSS
* 分析出Item. 解决desc中换行问题
* 将item存入本地SQLite.
* 实现分页查询
* 考虑到每次刷新需要比对时间, 把比数据库更新的item存入数据库, 时间使用long型存储. 并且提供适配.
###2013-08-09
* 实现item更新存入数据库.
* 实现Splash效果, 自动销毁.
* 改变主题为浅色Holo, Gmail主题
###2013-08-14
* 正文中去除所有htmltag
* 正文中获取第一张图片
###2013-08-15
* 使用bitmap多线程下载, 并且调整图片等比放大
* ListView 中图片文字等.


##TODO
* ListView 动态 下拉刷新, 上拉加载.
* 列表页 刷新 actionBar.
* Detail 页面中 解析 页面
* Detail 中分享

