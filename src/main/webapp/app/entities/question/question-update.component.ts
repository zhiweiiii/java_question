import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';
import format from 'date-fns/format';
import parse from 'date-fns/parse';
import parseISO from 'date-fns/parseISO';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import CommentService from '../comment/comment.service';
import { IComment } from '@/shared/model/comment.model';

import AlertService from '@/shared/alert/alert.service';
import { IQuestion, Question } from '@/shared/model/question.model';
import QuestionService from './question.service';

const validations: any = {
  question: {
    content: {},
    answer: {},
    createId: {},
    remark: {},
    star: {},
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
export default class QuestionUpdate extends mixins(JhiDataUtils) {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('questionService') private questionService: () => QuestionService;
  public question: IQuestion = new Question();

  @Inject('commentService') private commentService: () => CommentService;

  public comments: IComment[] = [];
  public isSaving = false;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.questionId) {
        vm.retrieveQuestion(to.params.questionId);
      }
      vm.initRelationships();
    });
  }

  public save(): void {
    this.isSaving = true;
    if (this.question.id) {
      this.questionService()
        .update(this.question)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Question is updated with identifier ' + param.id;
          this.alertService().showAlert(message, 'info');
        });
    } else {
      this.questionService()
        .create(this.question)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Question is created with identifier ' + param.id;
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
      this.question[field] = parse(event.target.value, DATE_TIME_LONG_FORMAT, new Date());
    } else {
      this.question[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.question[field] = parse(event.target.value, DATE_TIME_LONG_FORMAT, new Date());
    } else {
      this.question[field] = null;
    }
  }

  public retrieveQuestion(questionId): void {
    this.questionService()
      .find(questionId)
      .then(res => {
        res.createdDate = new Date(res.createdDate);
        res.lastModifiedDate = new Date(res.lastModifiedDate);
        this.question = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.commentService()
      .retrieve()
      .then(res => {
        this.comments = res.data;
      });
  }
}
