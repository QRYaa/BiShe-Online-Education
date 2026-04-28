import { useUserStore } from '@/store';

export function getToken() {
  const userStore = useUserStore();
  const { token } = userStore;
  return token;
}
