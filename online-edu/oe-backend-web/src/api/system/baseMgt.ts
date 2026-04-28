import { request } from '@/utils/request';

export function login(data: any) {
  return request.post<any>({
    url: '/login',
    data,
  });
}
export function getTicket() {
  return request.get<any>({
    url: '/admin/getTicket',
  });
}

export function logout() {
  return request.get({
    url: '/logout',
  });
}

export function captcha() {
  return request.get({
    url: '/captcha',
  });
}

export function uploadImg(file: File) {
  return request.upload('file', file, {
    url: '/admin/uploadImg',
  });
}

export function uploadLessonAudio(file: File) {
  return request.upload('file', file, {
    url: '/admin/lesson/uploadAudio',
  });
}

export function uploadLessonVideo(file: File) {
  return request.upload('file', file, {
    url: '/admin/lesson/uploadVideo',
  });
}

export function areaTree() {
  return request.get<any>({
    url: '/areaTree',
  });
}
