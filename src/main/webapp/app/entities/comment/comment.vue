<template>
    <div>
        <h2 id="page-heading">
            <span id="comment-heading">Comments</span>
            <router-link :to="{name: 'CommentCreate'}" tag="button" id="jh-create-entity" class="btn btn-primary float-right jh-create-entity create-comment">
                <font-awesome-icon icon="plus"></font-awesome-icon>
                <span >
                    Create a new Comment
                </span>
            </router-link>
        </h2>
        <b-alert :show="dismissCountDown"
            dismissible
            :variant="alertType"
            @dismissed="dismissCountDown=0"
            @dismiss-count-down="countDownChanged">
            {{alertMessage}}
        </b-alert>
        <br/>
        <div class="alert alert-warning" v-if="!isFetching && comments && comments.length === 0">
            <span>No comments found</span>
        </div>
        <div class="table-responsive" v-if="comments && comments.length > 0">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th v-on:click="changeOrder('id')"><span>ID</span> <font-awesome-icon icon="sort"></font-awesome-icon></th>
                    <th v-on:click="changeOrder('content')"><span>Content</span> <font-awesome-icon icon="sort"></font-awesome-icon></th>
                    <th v-on:click="changeOrder('createId')"><span>Create Id</span> <font-awesome-icon icon="sort"></font-awesome-icon></th>
                    <th v-on:click="changeOrder('createdBy')"><span>Created By</span> <font-awesome-icon icon="sort"></font-awesome-icon></th>
                    <th v-on:click="changeOrder('createdDate')"><span>Created Date</span> <font-awesome-icon icon="sort"></font-awesome-icon></th>
                    <th v-on:click="changeOrder('lastModifiedBy')"><span>Last Modified By</span> <font-awesome-icon icon="sort"></font-awesome-icon></th>
                    <th v-on:click="changeOrder('lastModifiedDate')"><span>Last Modified Date</span> <font-awesome-icon icon="sort"></font-awesome-icon></th>
                    <th v-on:click="changeOrder('questionId')"><span>Question</span> <font-awesome-icon icon="sort"></font-awesome-icon></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="comment in comments"
                    :key="comment.id">
                    <td>
                        <router-link :to="{name: 'CommentView', params: {commentId: comment.id}}">{{comment.id}}</router-link>
                    </td>
                    <td>{{comment.content}}</td>
                    <td>{{comment.createId}}</td>
                    <td>{{comment.createdBy}}</td>
                    <td>{{comment.createdDate | formatDate}}</td>
                    <td>{{comment.lastModifiedBy}}</td>
                    <td>{{comment.lastModifiedDate | formatDate}}</td>
                    <td>
                        <div v-if="comment.questionId">
                            <router-link :to="{name: 'QuestionView', params: {questionId: comment.questionId}}">{{comment.questionId}}</router-link>
                        </div>
                    </td>
                    <td class="text-right">
                        <div class="btn-group">
                            <router-link :to="{name: 'CommentView', params: {commentId: comment.id}}" tag="button" class="btn btn-info btn-sm details">
                                <font-awesome-icon icon="eye"></font-awesome-icon>
                                <span class="d-none d-md-inline">View</span>
                            </router-link>
                            <router-link :to="{name: 'CommentEdit', params: {commentId: comment.id}}"  tag="button" class="btn btn-primary btn-sm edit">
                                <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                                <span class="d-none d-md-inline">Edit</span>
                            </router-link>
                            <b-button v-on:click="prepareRemove(comment)"
                                   variant="danger"
                                   class="btn btn-sm"
                                   v-b-modal.removeEntity>
                                <font-awesome-icon icon="times"></font-awesome-icon>
                                <span class="d-none d-md-inline">Delete</span>
                            </b-button>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <b-modal ref="removeEntity" id="removeEntity" >
            <span slot="modal-title"><span id="javaQuestionApp.comment.delete.question">Confirm delete operation</span></span>
            <div class="modal-body">
                <p id="jhi-delete-comment-heading" >Are you sure you want to delete this Comment?</p>
            </div>
            <div slot="modal-footer">
                <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
                <button type="button" class="btn btn-primary" id="jhi-confirm-delete-comment" v-on:click="removeComment()">Delete</button>
            </div>
        </b-modal>
        <div v-show="comments && comments.length > 0">
            <div class="row justify-content-center">
                <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
            </div>
            <div class="row justify-content-center">
                <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./comment.component.ts">
</script>
