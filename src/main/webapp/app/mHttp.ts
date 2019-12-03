import axios from 'axios';
import { Indicator, MessageBox, Toast } from 'mint-ui';
import buildPaginationQueryOpts from '@/shared/sort/sorts';

// POST 方法封装  (参数处理)
export default function mHttp(method, url, data = {}) {
  Indicator.open({
    text: '加载中...',
    spinnerType: 'fading-circle'
  });
  return new Promise<any>((resolve, reject) => {
    var baseUrl = 'localhost:9061/';
    var config = {
      methods: method,
      url: baseUrl + url,
      data: data
    };
    axios(config).then(
      response => {
        if (response.data.status == 400) {
          resolve(response);
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

export function mHttpWithPage(method, url, data = {}, paginationQuery) {
  Indicator.open({
    text: '加载中...',
    spinnerType: 'fading-circle'
  });
  console.log('123');
  return new Promise<any>((resolve, reject) => {
    var baseUrl = 'http://localhost:9061/';
    var config = {
      methods: method,
      url: baseUrl + url + buildPaginationQueryOpts(paginationQuery),
      data: data
    };
    console.log(config);
    axios(config).then(
      response => {
        console.log(response);
        if (response.data.status == 400) {
          resolve(response);
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
