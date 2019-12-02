import axios from 'axios';
import { Indicator, MessageBox, Toast } from 'mint-ui';

// POST 方法封装  (参数处理)
export function mHttp(method, url, data = {}) {
  Indicator.open({
    text: '加载中...',
    spinnerType: 'fading-circle'
  });
  return new Promise((resolve, reject) => {
    var baseUrl = 'localhost:1080/';
    var config = {
      methods: method,
      url: baseUrl + url,
      data: data
    };
    axios(config).then(
      response => {
        if (response.data.status == 400) {
          resolve(response.data);
        } else {
          Toast(response.data.message);
        }
        Indicator.close(); // // 关闭动画
      },
      err => {
        reject(err);
        Indicator.close();
        Toast(err.message);
      }
    );
  });
}
