/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import CommentDetailComponent from '@/entities/comment/comment-details.vue';
import CommentClass from '@/entities/comment/comment-details.component';
import CommentService from '@/entities/comment/comment.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Comment Management Detail Component', () => {
    let wrapper: Wrapper<CommentClass>;
    let comp: CommentClass;
    let commentServiceStub: SinonStubbedInstance<CommentService>;

    beforeEach(() => {
      commentServiceStub = sinon.createStubInstance<CommentService>(CommentService);

      wrapper = shallowMount<CommentClass>(CommentDetailComponent, {
        store,
        localVue,
        provide: { commentService: () => commentServiceStub }
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundComment = { id: 123 };
        commentServiceStub.find.resolves(foundComment);

        // WHEN
        comp.retrieveComment(123);
        await comp.$nextTick();

        // THEN
        expect(comp.comment).toBe(foundComment);
      });
    });
  });
});
