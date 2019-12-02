<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="question">
                <h2 class="jh-entity-heading"><span>Question</span> {{question.id}}</h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span>Content</span>
                    </dt>
                    <dd>
                        <span>{{question.content}}</span>
                    </dd>
                    <dt>
                        <span>Answer</span>
                    </dt>
                    <dd>
                        <span>{{question.answer}}</span>
                    </dd>
                    <dt>
                        <span>Create Id</span>
                    </dt>
                    <dd>
                        <span>{{question.createId}}</span>
                    </dd>
                    <dt>
                        <span>Remark</span>
                    </dt>
                    <dd>
                        <span>{{question.remark}}</span>
                    </dd>
                    <dt>
                        <span>Star</span>
                    </dt>
                    <dd>
                        <span>{{question.star}}</span>
                    </dd>
                    <dt>
                        <span>Created By</span>
                    </dt>
                    <dd>
                        <span>{{question.createdBy}}</span>
                    </dd>
                    <dt>
                        <span>Created Date</span>
                    </dt>
                    <dd>
                        <span>{{question.createdDate | formatDate}}</span>
                    </dd>
                    <dt>
                        <span>Last Modified By</span>
                    </dt>
                    <dd>
                        <span>{{question.lastModifiedBy}}</span>
                    </dd>
                    <dt>
                        <span>Last Modified Date</span>
                    </dt>
                    <dd>
                        <span>{{question.lastModifiedDate | formatDate}}</span>
                    </dd>
                </dl>
                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
                </button>
                <router-link v-if="question.id" :to="{name: 'QuestionEdit', params: {questionId: question.id}}" tag="button" class="btn btn-primary">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./question-details.component.ts">
</script>
