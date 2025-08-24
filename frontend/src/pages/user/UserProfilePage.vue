<template>
  <div class="user-profile-page">
    <a-card title="个人资料" class="profile-card">
      <template #extra>
        <a-button type="primary" @click="showEditModal = true">编辑</a-button>
        <a-button style="margin-left: 12px;" @click="goToChangePassword">修改密码</a-button>
      </template>
      <div class="profile-header">
        <div class="avatar-upload-wrapper" @click="showAvatarUpload = true">
          <a-avatar :size="96" :src="user.userAvatar" style="cursor:pointer;" />
          <div class="avatar-upload-mask">
            <span class="mask-main">点击更换</span>
            <span class="mask-sub">头像</span>
          </div>
        </div>
        <div class="profile-username">{{ user.userName || '无名' }}</div>
        <div class="profile-role">{{ user.userRole ? (user.userRole === 'admin' ? '管理员' : '普通用户') : '' }}</div>
      </div>
      <a-descriptions :column="2" bordered class="profile-desc">
        <a-descriptions-item label="ID">{{ user.id || '-' }}</a-descriptions-item>
        <a-descriptions-item label="账号">{{ user.userAccount || '-' }}</a-descriptions-item>
        <a-descriptions-item label="用户简介" :span="2">{{ user.userProfile || '-' }}</a-descriptions-item>
        <a-descriptions-item label="注册时间" :span="2">{{ user.createTime ? formatDate(user.createTime) : '未知' }}</a-descriptions-item>
      </a-descriptions>
    </a-card>

    <a-modal v-model:visible="showEditModal" title="编辑个人资料" @ok="handleEditSubmit" :confirm-loading="editLoading" @cancel="handleEditCancel">
      <a-form :model="editForm" layout="vertical">
        <a-form-item label="昵称">
          <a-input v-model:value="editForm.userName" placeholder="请输入昵称" />
        </a-form-item>
        <a-form-item label="用户简介">
          <a-input v-model:value="editForm.userProfile" placeholder="请输入简介" />
        </a-form-item>
      </a-form>
    </a-modal>

    <a-modal v-model:visible="showAvatarUpload" title="上传头像" @cancel="showAvatarUpload = false" :footer="null">
      <PictureUpload :picture="{ url: user.userAvatar }" :onSuccess="onAvatarUploadSuccess" />
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useLoginUserStore } from '@/stores/useLoginUserStore';
import { getUserByIdUsingGet, editUserUsingPost } from '@/api/userController';
import { message } from 'ant-design-vue';
import PictureUpload from '@/components/PictureUpload.vue';
import { useRouter } from 'vue-router';

const loginUserStore = useLoginUserStore();
const loginUser = computed(() => loginUserStore.loginUser || {});
const user = ref<any>({});
const loading = ref(false);
const showEditModal = ref(false);
const editLoading = ref(false);
const editForm = ref({ userName: '', userProfile: '', userAvatar: '' });
const showAvatarUpload = ref(false);
const router = useRouter();

function formatDate(dateStr: string) {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  return date.toLocaleDateString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit' });
}

async function fetchUserProfile() {
  if (!loginUser.value.id) return;
  loading.value = true;
  try {
    const res = await getUserByIdUsingGet({ id: loginUser.value.id });
    if (res.data.code === 0 && res.data.data) {
      user.value = res.data.data;
    }
  } finally {
    loading.value = false;
  }
}

function openEditModal() {
  editForm.value = {
    userName: user.value.userName,
    userProfile: user.value.userProfile,
    userAvatar: user.value.userAvatar,
  };
  showEditModal.value = true;
}

function handleEditCancel() {
  showEditModal.value = false;
}

async function handleEditSubmit() {
  editLoading.value = true;
  try {
    // 只提交有内容的字段
    const payload: any = { id: user.value.id };
    if (editForm.value.userName && editForm.value.userName.trim() !== '') {
      payload.userName = editForm.value.userName;
    }
    if (editForm.value.userProfile && editForm.value.userProfile.trim() !== '') {
      payload.userProfile = editForm.value.userProfile;
    }
    const res = await editUserUsingPost(payload);
    if (res.data.code === 0) {
      message.success('资料更新成功');
      showEditModal.value = false;
      fetchUserProfile();
    } else {
      message.error(res.data.message || '更新失败');
    }
  } catch (e) {
    message.error('请求失败');
  } finally {
    editLoading.value = false;
  }
}

async function onAvatarUploadSuccess(newPic) {
  // newPic.url 为新头像链接
  await editUserUsingPost({
    id: user.value.id,
    userAvatar: newPic.url,
    userName: user.value.userName,
    userProfile: user.value.userProfile,
  });
  showAvatarUpload.value = false;
  fetchUserProfile();
}

function goToChangePassword() {
  router.push('/user/changePassword');
}

onMounted(() => {
  if (!loginUser.value.id) {
    loginUserStore.fetchLoginUser().then(fetchUserProfile);
  } else {
    fetchUserProfile();
  }
});
</script>

<style scoped>
.user-profile-page {
  min-height: 60vh;
  background: linear-gradient(135deg, #e0e7ff 0%, #f5f7fa 100%);
  padding: 60px 0;
}
.profile-card {
  max-width: 650px;
  margin: 0 auto;
  box-shadow: 0 8px 32px rgba(80,120,200,0.10);
  border-radius: 20px;
  padding: 40px 0 48px 0;
  background: #fff;
}
.a-card.profile-card .ant-card-head-title {
  font-size: 1.35rem;
  font-weight: 700;
  color: #2d3a4b;
  letter-spacing: 2px;
  text-align: left;
  padding-left: 8px;
}
.a-card.profile-card .ant-card-head {
  background: transparent;
  border-top-left-radius: 20px;
  border-top-right-radius: 20px;
  padding-top: 10px;
  padding-bottom: 10px;
}
.profile-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 48px;
}
.avatar-upload-wrapper {
  position: relative;
  display: inline-block;
  cursor: pointer;
  margin-bottom: 8px;
}
.avatar-upload-wrapper .ant-avatar {
  border: 4px solid #fff;
  box-shadow: 0 4px 24px 0 rgba(79,140,255,0.18), 0 0 0 4px #4f8cff;
  background: linear-gradient(135deg, #e0e7ff 0%, #f4f8ff 100%);
  transition: box-shadow 0.2s, border 0.2s;
}
.avatar-upload-wrapper:hover .ant-avatar {
  box-shadow: 0 8px 32px 0 rgba(37,99,235,0.25), 0 0 0 4px #2563eb;
  border-color: #e0e7ff;
}
.avatar-upload-mask {
  position: absolute;
  left: 0; right: 0; bottom: 0; top: 0;
  background: rgba(79,140,255,0.18);
  color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  opacity: 0;
  border-radius: 50%;
  font-size: 1.08rem;
  font-weight: 500;
  letter-spacing: 1px;
  transition: opacity 0.2s;
  pointer-events: none;
  z-index: 2;
}
.avatar-upload-wrapper:hover .avatar-upload-mask {
  opacity: 1;
  pointer-events: auto;
}
.mask-main {
  font-size: 1.08rem;
  font-weight: 600;
  letter-spacing: 1px;
  line-height: 1.2;
}
.mask-sub {
  font-size: 1.18rem;
  font-weight: 700;
  margin-top: 2px;
  letter-spacing: 2px;
  line-height: 1.2;
}
.profile-username {
  font-size: 2rem;
  font-weight: 700;
  margin-top: 20px;
  color: #2d3a4b;
  letter-spacing: 1px;
}
.profile-role {
  font-size: 1.15rem;
  color: #6b7280;
  margin-top: 8px;
}
.profile-desc {
  background: #f8fafc;
  border-radius: 12px;
  padding: 18px 24px;
  font-size: 1.08rem;
}
:deep(.ant-descriptions-item-label) {
  width: 120px;
  color: #4b5563;
  font-weight: 600;
  background: #f1f5f9;
}
:deep(.ant-descriptions-item-content) {
  color: #334155;
}
</style>
