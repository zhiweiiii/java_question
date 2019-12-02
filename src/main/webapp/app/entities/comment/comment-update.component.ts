import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';
import format from 'date-fns/format';
import parse from 'date-fns/parse';
import parseISO from 'date-fns/parseISO';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import QuestionService from '../question/question.service';
import { IQuestion } from '@/shared/model/question.model';

import AlertService from '@/shared/alert/alert.service';
import { IComment, Comment } from '@/shared/model/comment.model';
import CommentService from './comment.service';

const validations: any = {
  comment: {
    content: {},
    createId: {},
    createdBy: {
      maxLength: maxLength(50)
    },
    createdDate: {},
    lastModifiedBy: {
      maxLength: maxLength(50)
    },
    lastModifiedDate: {}
  }
};

@Component({
  validations
})
export default class CommentUpdate extends mixins(JhiDataUtils) {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('commentService') private commentService: () => CommentService;
  public comment: IComment = new Comment();

  @Inject('questionService') private questionService: () => QuestionService;

  public questions: IQuestion[] = [];
  public isSaving = false;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.commentId) {
        vm.retrieveComment(to.params.commentId);
      }
      vm.initRelationships();
    });
  }

  public save(): void {
    this.isSaving = true;
    if (this.comment.id) {
      this.commentService()
        .update(this.comment)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Comment is updated with identifier ' + param.id;
          this.alertService().showAlert(message, 'info');
        });
    } else {
      this.commentService()
        .create(this.comment)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Comment is created with identifier ' + param.id;
          this.alertService().showAlert(message, 'success');
        });
    }
  }

  public convertDateTimeFromServer(date: Date): string {
    if (date) {
      return format(date, DATE_TIME_LONG_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.comment[field] = parse(event.target.value, DATE_TIME_LONG_FORMAT, new Date());
    } else {
      this.comment[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.comment[field] = parse(event.target.value, DATE_TIME_LONG_FORMAT, new Date());
    } else {
      this.comment[field] = null;
    }
  }

  public retrieveComment(commentId): void {
    this.commentService()
      .find(commentId)
      .then(res => {
        res.createdDate = new Date(res.createdDate);
        res.lastModifiedDate = new Date(res.lastModifiedDate);
        this.comment = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.questionService()
      .retrieve()
      .then(res => {
        this.questions = res.data;
      });
  }
}
