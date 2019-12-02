import { IComment } from '@/shared/model/comment.model';

export interface IQuestion {
  id?: number;
  content?: any;
  answer?: any;
  createId?: number;
  remark?: any;
  star?: number;
  createdBy?: string;
  createdDate?: Date;
  lastModifiedBy?: string;
  lastModifiedDate?: Date;
  comments?: IComment[];
}

export class Question implements IQuestion {
  constructor(
    public id?: number,
    public content?: any,
    public answer?: any,
    public createId?: number,
    public remark?: any,
    public star?: number,
    public createdBy?: string,
    public createdDate?: Date,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Date,
    public comments?: IComment[]
  ) {}
}
