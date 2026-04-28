import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsTeacherTag/page',
    params,
  });
}
export function listAll() {
  return request.get<any>({
    url: '/admin/bsTeacherTag/listAll',
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/bsTeacherTag/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/bsTeacherTag/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/bsTeacherTag/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/bsTeacherTag/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/bsTeacherTag/delete',
    data,
  });
}
