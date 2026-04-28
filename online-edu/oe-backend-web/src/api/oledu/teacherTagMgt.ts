import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/oeTeacherTag/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/oeTeacherTag/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/oeTeacherTag/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/oeTeacherTag/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/oeTeacherTag/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/oeTeacherTag/delete',
    data,
  });
}

export function listAll() {
  return request.get<any>({
    url: '/admin/oeTeacherTag/listAll',
  });
}
