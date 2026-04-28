import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/oeCourseType/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/oeCourseType/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/oeCourseType/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/oeCourseType/get/${id}`,
  });
}
export function listAll() {
  return request.get<any>({
    url: '/admin/oeCourseType/listAll',
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/oeCourseType/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/oeCourseType/delete',
    data,
  });
}
