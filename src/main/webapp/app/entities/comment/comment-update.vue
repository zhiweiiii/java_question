<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" >
                <h2 id="javaQuestionApp.comment.home.createOrEditLabel">Create or edit a Comment</h2>
                <div>
                    <div class="form-group" v-if="comment.id">
                        <label for="id">ID</label>
                        <input type="text" class="form-control" id="id" name="id"
                               v-model="comment.id" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="comment-content">Content</label>
                        <textarea class="form-control" name="content" id="comment-content"
                            :class="{'valid': !$v.comment.content.$invalid, 'invalid': $v.comment.content.$invalid }" v-model="$v.comment.content.$model" ></textarea>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="comment-createId">Create Id</label>
                        <input type="number" class="form-control" name="createId" id="comment-createId"
                            :class="{'valid': !$v.comment.createId.$invalid, 'invalid': $v.comment.createId.$invalid }" v-model.number="$v.comment.createId.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="comment-createdBy">Created By</label>
                        <input type="text" class="form-control" name="createdBy" id="comment-createdBy"
                            :class="{'valid': !$v.comment.createdBy.$invalid, 'invalid': $v.comment.createdBy.$invalid }" v-model="$v.comment.createdBy.$model" />
                        <div v-if="$v.comment.createdBy.$anyDirty && $v.comment.createdBy.$invalid">
                            <small class="form-text text-danger" v-if="!$v.comment.createdBy.maxLength" >
                                This field cannot be longer than 50 characters.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="comment-createdDate">Created Date</label>
                        <div class="d-flex">
                            <input id="comment-createdDate" type="datetime-local" class="form-control" name="createdDate" :class="{'valid': !$v.comment.createdDate.$invalid, 'invalid': $v.comment.createdDate.$invalid }"
                            
                            :value="convertDateTimeFromServer($v.comment.createdDate.$model)"
                            @change="updateInstantField('createdDate', $event)"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="comment-lastModifiedBy">Last Modified By</label>
                        <input type="text" class="form-control" name="lastModifiedBy" id="comment-lastModifiedBy"
                            :class="{'valid': !$v.comment.lastModifiedBy.$invalid, 'invalid': $v.comment.lastModifiedBy.$invalid }" v-model="$v.comment.lastModifiedBy.$model" />
                        <div v-if="$v.comment.lastModifiedBy.$anyDirty && $v.comment.lastModifiedBy.$invalid">
                            <small class="form-text text-danger" v-if="!$v.comment.lastModifiedBy.maxLength" >
                                This field cannot be longer than 50 characters.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="comment-lastModifiedDate">Last Modified Date</label>
                        <div class="d-flex">
                            <input id="comment-lastModifiedDate" type="datetime-local" class="form-control" name="lastModifiedDate" :class="{'valid': !$v.comment.lastModifiedDate.$invalid, 'invalid': $v.comment.lastModifiedDate.$invalid }"
                            
                            :value="convertDateTimeFromServer($v.comment.lastModifiedDate.$model)"
                            @change="updateInstantField('lastModifiedDate', $event)"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label"  for="comment-question">Question</label>
                        <select class="form-control" id="comment-question" name="question" v-model="comment.questionId">
                            <option v-bind:value="null"></option>
                            <option v-bind:value="questionOption.id" v-for="questionOption in questions" :key="questionOption.id">{{questionOption.id}}</option>
                        </select>
                    </div>
                </div>
                <div>
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" :disabled="$v.comment.$invalid || isSaving" class="btn btn-primary">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./comment-update.component.ts">
</script>
