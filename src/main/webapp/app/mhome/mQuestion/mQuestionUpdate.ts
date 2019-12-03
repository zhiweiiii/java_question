import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';
import format from 'date-fns/format';
import parse from 'date-fns/parse';
import parseISO from 'date-fns/parseISO';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import { IComment } from '@/shared/model/comment.model';

import AlertService from '@/shared/alert/alert.service';
import { IQuestion, Question } from '@/shared/model/question.model';

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
  public question: IQuestion = new Question();
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

  public previousState(): void {
    this.$router.go(-1);
  }
}
