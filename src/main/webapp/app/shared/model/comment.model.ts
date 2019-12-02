export interface IComment {
  id?: number;
  content?: any;
  createId?: number;
  createdBy?: string;
  createdDate?: Date;
  lastModifiedBy?: string;
  lastModifiedDate?: Date;
  questionId?: number;
}

export class Comment implements IComment {
  constructor(
    public id?: number,
    public content?: any,
    public createId?: number,
    public createdBy?: string,
    public createdDate?: Date,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Date,
    public questionId?: number
  ) {}
}
