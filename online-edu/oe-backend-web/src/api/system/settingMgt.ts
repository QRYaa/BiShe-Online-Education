import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsSetting/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/bsSetting/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/bsSetting/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/bsSetting/get/${id}`,
  });
}
export function load(code: any) {
  return request.get<any>({
    url: `/admin/bsSetting/load/${code}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/bsSetting/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/bsSetting/delete',
    data,
  });
}
