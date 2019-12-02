<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="comment">
                <h2 class="jh-entity-heading"><span>Comment</span> {{comment.id}}</h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span>Content</span>
                    </dt>
                    <dd>
                        <span>{{comment.content}}</span>
                    </dd>
                    <dt>
                        <span>Create Id</span>
                    </dt>
                    <dd>
                        <span>{{comment.createId}}</span>
                    </dd>
                    <dt>
                        <span>Created By</span>
                    </dt>
                    <dd>
                        <span>{{comment.createdBy}}</span>
                    </dd>
                    <dt>
                        <span>Created Date</span>
                    </dt>
                    <dd>
                        <span>{{comment.createdDate | formatDate}}</span>
                    </dd>
                    <dt>
                        <span>Last Modified By</span>
                    </dt>
                    <dd>
                        <span>{{comment.lastModifiedBy}}</span>
                    </dd>
                    <dt>
                        <span>Last Modified Date</span>
                    </dt>
                    <dd>
                        <span>{{comment.lastModifiedDate | formatDate}}</span>
                    </dd>
                    <dt>
                        <span>Question</span>
                    </dt>
                    <dd>
                        <div v-if="comment.questionId">
                            <router-link :to="{name: 'QuestionView', params: {questionId: comment.questionId}}">{{comment.questionId}}</router-link>
                        </div>
                    </dd>
                </dl>
                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
                </button>
                <router-link v-if="comment.id" :to="{name: 'CommentEdit', params: {commentId: comment.id}}" tag="button" class="btn btn-primary">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./comment-details.component.ts">
</script>
