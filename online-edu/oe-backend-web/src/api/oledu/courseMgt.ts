import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/oeCourse/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/oeCourse/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/oeCourse/update',
    data,
  });
}
export function changeStatus(data: any) {
  return request.put<any>({
    url: '/admin/oeCourse/changeStatus',
    data,
  });
}
export function switchPaid(data: any) {
  return request.put<any>({
    url: '/admin/oeCourse/switchPaid',
    data,
  });
}
export function switchPublished(data: any) {
  return request.put<any>({
    url: '/admin/oeCourse/switchPublished',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/oeCourse/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/oeCourse/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/oeCourse/delete',
    data,
  });
}
