import axios from 'axios';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { IQuestion } from '@/shared/model/question.model';

const baseApiUrl = 'api/questions';

export default class QuestionService {
  public find(id: number): Promise<IQuestion> {
    return new Promise<IQuestion>(resolve => {
      axios.get(`${baseApiUrl}/${id}`).then(function(res) {
        resolve(res.data);
      });
    });
  }

  public retrieve(paginationQuery?: any): Promise<any> {
    return new Promise<any>(resolve => {
      axios.get(baseApiUrl + `?${buildPaginationQueryOpts(paginationQuery)}`).then(function(res) {
        resolve(res);
      });
    });
  }

  public delete(id: number): Promise<any> {
    return new Promise<any>(resolve => {
      axios.delete(`${baseApiUrl}/${id}`).then(function(res) {
        resolve(res);
      });
    });
  }

  public create(entity: IQuestion): Promise<IQuestion> {
    return new Promise<IQuestion>(resolve => {
      axios.post(`${baseApiUrl}`, entity).then(function(res) {
        resolve(res.data);
      });
    });
  }

  public update(entity: IQuestion): Promise<IQuestion> {
    return new Promise<IQuestion>(resolve => {
      axios.put(`${baseApiUrl}`, entity).then(function(res) {
        resolve(res.data);
      });
    });
  }
}
