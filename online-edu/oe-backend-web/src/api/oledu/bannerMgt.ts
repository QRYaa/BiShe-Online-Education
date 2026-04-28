import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/oeBanner/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/oeBanner/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/oeBanner/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/oeBanner/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/oeBanner/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/oeBanner/delete',
    data,
  });
}

export function updateEnableTrueById(data: any) {
  return request.post<any>({
    url: '/admin/oeBanner/updateEnableTrueById',
    data,
  });
}

export function updateEnableFalseById(data: any) {
  return request.post<any>({
    url: '/admin/oeBanner/updateEnableFalseById',
    data,
  });
}
