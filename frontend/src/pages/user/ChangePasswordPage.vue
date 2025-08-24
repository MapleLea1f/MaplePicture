<template>
  <div class="change-password-page">
    <a-card title="修改密码" class="change-password-card">
      <a-form :model="formState" @finish="handleSubmit" layout="vertical">
        <a-form-item label="账号" name="userAccount" :rules="[{ required: true, message: '请输入账号' }]">
          <a-input v-model:value="formState.userAccount" placeholder="请输入账号" />
        </a-form-item>
        <a-form-item label="新密码" name="newPassword" :rules="[{ required: true, message: '请输入新密码' }, { min: 8, message: '密码不能小于8位' }]">
          <a-input-password v-model:value="formState.newPassword" placeholder="请输入新密码" />
        </a-form-item>
        <a-form-item label="确认新密码" name="confirmPassword" :rules="[{ required: true, message: '请确认新密码' }, { validator: validateConfirmPassword }]">
          <a-input-password v-model:value="formState.confirmPassword" placeholder="请再次输入新密码" />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit" :loading="loading" style="width: 100%">提交</a-button>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { message } from 'ant-design-vue';
import { useRouter } from 'vue-router';
// 假设有 changeUserPasswordUsingPost 方法
import { changePasswordUsingPost } from '@/api/userController'

const router = useRouter();
const loading = ref(false);
const formState = reactive({
  userAccount: '',
  newPassword: '',
  confirmPassword: '',
});

const validateConfirmPassword = (_: any, value: string) => {
  if (value !== formState.newPassword) {
    return Promise.reject('两次输入的密码不一致');
  }
  return Promise.resolve();
};

const handleSubmit = async () => {
  if (!formState.userAccount || !formState.newPassword || !formState.confirmPassword) {
    message.error('请填写完整信息');
    return;
  }
  loading.value = true;
  try {
    const res = await changePasswordUsingPost({
      userAccount: formState.userAccount,
      newPassword: formState.newPassword,
    });
    if (res.data.code === 0) {
      message.success('密码修改成功，请重新登录');
      router.push('/user/login');
    } else {
      message.error(res.data.message || '密码修改失败');
    }
  } catch (e) {
    message.error('请求失败');
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.change-password-page {
  min-height: 60vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #e0e7ff 0%, #f5f7fa 100%);
}
.change-password-card {
  width: 360px;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(80,120,200,0.10);
}
</style>

