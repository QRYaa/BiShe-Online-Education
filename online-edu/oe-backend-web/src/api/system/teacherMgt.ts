import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsTeacher/page',
    params,
  });
}
export function listAll() {
  return request.get<any>({
    url: '/admin/bsTeacher/listAll',
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/bsTeacher/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/bsTeacher/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/bsTeacher/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/bsTeacher/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/bsTeacher/delete',
    data,
  });
}
