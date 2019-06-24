
##### 保证接口幂等性，表单重复提交


```
前台解决方案：
提交后按钮禁用、置灰、页面出现遮罩


后台解决方案:   使用token，每个token只能使用一次
1.在调用接口之前生成对应的Token，存放至redis
2.在调用接口时，将生成的令牌放入请求头中
3.接口提交的时候获取对应的令牌，如果能够从redis中获得该令牌(获取后将当前令牌删除)，
则继续执行访问的业务逻辑
4.接口提交的时候获取对应的令牌，如果获取不到改令牌，则直接返回请勿提交

```

博客园:https://www.cnblogs.com/youxiu326/p/more_submit.html