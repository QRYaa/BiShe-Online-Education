import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/oeTeacher/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/oeTeacher/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/oeTeacher/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/oeTeacher/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/oeTeacher/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/oeTeacher/delete',
    data,
  });
}

export function listAll() {
  return request.get<any>({
    url: '/admin/oeTeacher/listAll',
  });
}
