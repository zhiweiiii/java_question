<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" >
                <h2 id="javaQuestionApp.question.home.createOrEditLabel">Create or edit a Question</h2>
                <div>
                    <div class="form-group" v-if="question.id">
                        <label for="id">ID</label>
                        <input type="text" class="form-control" id="id" name="id"
                               v-model="question.id" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="question-content">Content</label>
                        <textarea class="form-control" name="content" id="question-content"
                            :class="{'valid': !$v.question.content.$invalid, 'invalid': $v.question.content.$invalid }" v-model="$v.question.content.$model" ></textarea>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="question-answer">Answer</label>
                        <textarea class="form-control" name="answer" id="question-answer"
                            :class="{'valid': !$v.question.answer.$invalid, 'invalid': $v.question.answer.$invalid }" v-model="$v.question.answer.$model" ></textarea>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="question-createId">Create Id</label>
                        <input type="number" class="form-control" name="createId" id="question-createId"
                            :class="{'valid': !$v.question.createId.$invalid, 'invalid': $v.question.createId.$invalid }" v-model.number="$v.question.createId.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="question-remark">Remark</label>
                        <textarea class="form-control" name="remark" id="question-remark"
                            :class="{'valid': !$v.question.remark.$invalid, 'invalid': $v.question.remark.$invalid }" v-model="$v.question.remark.$model" ></textarea>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="question-star">Star</label>
                        <input type="number" class="form-control" name="star" id="question-star"
                            :class="{'valid': !$v.question.star.$invalid, 'invalid': $v.question.star.$invalid }" v-model.number="$v.question.star.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="question-createdBy">Created By</label>
                        <input type="text" class="form-control" name="createdBy" id="question-createdBy"
                            :class="{'valid': !$v.question.createdBy.$invalid, 'invalid': $v.question.createdBy.$invalid }" v-model="$v.question.createdBy.$model" />
                        <div v-if="$v.question.createdBy.$anyDirty && $v.question.createdBy.$invalid">
                            <small class="form-text text-danger" v-if="!$v.question.createdBy.maxLength" >
                                This field cannot be longer than 50 characters.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="question-createdDate">Created Date</label>
                        <div class="d-flex">
                            <input id="question-createdDate" type="datetime-local" class="form-control" name="createdDate" :class="{'valid': !$v.question.createdDate.$invalid, 'invalid': $v.question.createdDate.$invalid }"
                            
                            :value="convertDateTimeFromServer($v.question.createdDate.$model)"
                            @change="updateInstantField('createdDate', $event)"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="question-lastModifiedBy">Last Modified By</label>
                        <input type="text" class="form-control" name="lastModifiedBy" id="question-lastModifiedBy"
                            :class="{'valid': !$v.question.lastModifiedBy.$invalid, 'invalid': $v.question.lastModifiedBy.$invalid }" v-model="$v.question.lastModifiedBy.$model" />
                        <div v-if="$v.question.lastModifiedBy.$anyDirty && $v.question.lastModifiedBy.$invalid">
                            <small class="form-text text-danger" v-if="!$v.question.lastModifiedBy.maxLength" >
                                This field cannot be longer than 50 characters.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="question-lastModifiedDate">Last Modified Date</label>
                        <div class="d-flex">
                            <input id="question-lastModifiedDate" type="datetime-local" class="form-control" name="lastModifiedDate" :class="{'valid': !$v.question.lastModifiedDate.$invalid, 'invalid': $v.question.lastModifiedDate.$invalid }"
                            
                            :value="convertDateTimeFromServer($v.question.lastModifiedDate.$model)"
                            @change="updateInstantField('lastModifiedDate', $event)"/>
                        </div>
                    </div>
                </div>
                <div>
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" :disabled="$v.question.$invalid || isSaving" class="btn btn-primary">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./question-update.component.ts">
</script>
